package com.sistemas.distribuidos.grpc_gateway.service.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "hola-mundo-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void enviarMensajeHolaMundo(String mensaje) {
        logger.info("Enviando mensaje a Kafka: {}", mensaje);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, mensaje);

        future.whenComplete((result, failure) -> {
            if (failure == null) {
                logger.info("Mensaje enviado exitosamente: [{}] con offset=[{}]",
                        mensaje, result.getRecordMetadata().offset());
            } else {
                logger.error("Error al enviar mensaje: [{}] debido a: {}", mensaje, failure.getMessage());
            }
        });
    }
}
