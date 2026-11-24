package com.example.api_usuarios.Auditoria;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Component
public class AuditoriaProducer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaProducer.class);

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public AuditoriaProducer(SqsClient sqsClient,
                             @Value("${aws.sqs.auditoria-url:}") String queueUrl,
                             ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
    }

    public void enviarEvento(String evento, String referenciaId) {
        // En local (sin perfil prod) no hay cola configurada: no se intenta enviar nada.
        if (queueUrl == null || queueUrl.isBlank()) {
            log.warn("[SQS] Cola de auditoria no configurada; se omite evento={}, referenciaId={}",
                    evento, referenciaId);
            return;
        }

        String mensaje;
        try {
            // JSON simple: {"evento":"...","referenciaId":"..."}
            mensaje = objectMapper.writeValueAsString(Map.of(
                    "evento", evento,
                    "referenciaId", referenciaId));
        } catch (JacksonException e) {
            throw new IllegalStateException("No se pudo serializar el evento de auditoria", e);
        }

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(mensaje)
                .build();

        SendMessageResponse response = sqsClient.sendMessage(request);

        log.info("[SQS] Evento enviado → evento={}, referenciaId={}, messageId={}",
                evento, referenciaId, response.messageId());
        System.out.println("[SQS] Enviado: " + mensaje + " | messageId=" + response.messageId());
    }
}
