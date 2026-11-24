package com.example.api_usuarios.Auditoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.api_usuarios.Model.AuditoriaEvento;
import com.example.api_usuarios.Repository.AuditoriaEventoRepository;
import tools.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
public class AuditoriaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaConsumer.class);

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;
    private final AuditoriaEventoRepository auditoriaEventoRepository;

    public AuditoriaConsumer(SqsClient sqsClient,
                             @Value("${aws.sqs.auditoria-url:}") String queueUrl,
                             ObjectMapper objectMapper,
                             AuditoriaEventoRepository auditoriaEventoRepository) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
        this.auditoriaEventoRepository = auditoriaEventoRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void procesarMensajes() {
        // En local (sin perfil prod) no hay cola configurada: el scheduler no hace nada.
        if (queueUrl == null || queueUrl.isBlank()) {
            return;
        }

        try {
            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(5)
                    .build();

            ReceiveMessageResponse response = sqsClient.receiveMessage(request);

            if (response.messages().isEmpty()) {
                return;
            }

            log.info("[SQS] Recibidos {} mensaje(s) de auditoria", response.messages().size());
            System.out.println("[SQS] Recibidos " + response.messages().size() + " mensaje(s)");

            for (Message mensaje : response.messages()) {
                try {
                    AuditoriaEvento evento = objectMapper.readValue(mensaje.body(), AuditoriaEvento.class);

                    AuditoriaEvento guardado = auditoriaEventoRepository.save(evento);

                    log.info("[AUDITORIA] Guardado en RDS → id={}, evento={}, referenciaId={}",
                            guardado.getId(), guardado.getEvento(), guardado.getReferenciaId());
                    System.out.println("[AUDITORIA] Guardado: " + guardado.getEvento()
                            + " | referenciaId=" + guardado.getReferenciaId());

                    // Solo se borra el mensaje DESPUES de que el guardado fue exitoso
                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(mensaje.receiptHandle())
                            .build());

                    log.info("[SQS] Mensaje eliminado → messageId={}", mensaje.messageId());

                } catch (Exception e) {
                    // No se elimina el mensaje: SQS lo hara visible de nuevo para reintentar
                    log.error("[AUDITORIA] Error procesando messageId={}: {}",
                            mensaje.messageId(), e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            // Evita que una caida de SQS/RDS detenga la aplicacion o el scheduler
            log.error("[AUDITORIA] Error en el ciclo de consumo SQS: {}", e.getMessage(), e);
        }
    }
}
