package com.example.api_usuarios.Auditoria;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    // DefaultCredentialsProvider sigue la cadena estandar: variables de entorno,
    // propiedades del sistema e IMDS (rol IAM LabInstanceProfile de la EC2).
    // NO se usan AWS_ACCESS_KEY_ID ni AWS_SECRET_ACCESS_KEY quemados en codigo.
    @Bean
    public SqsClient sqsClient(@Value("${aws.region:us-east-1}") String region) {
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
