package com.sistemas.distribuidos.grpc_gateway.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "hola-mundo-topic", groupId = "grpc-gateway-group")
    public void escucharMensajesHolaMundo(String mensaje) {
        logger.info("Mensaje recibido desde Kafka: {}", mensaje);

        procesarMensaje(mensaje);
    }

    private void procesarMensaje(String mensaje) {
        logger.info("Procesando mensaje: {}", mensaje);

        if (mensaje.toLowerCase().contains("hola")) {
            logger.info("¡Mensaje de saludo detectado! 👋");
        }
    }
}
