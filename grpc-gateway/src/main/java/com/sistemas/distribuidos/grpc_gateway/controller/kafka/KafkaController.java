package com.sistemas.distribuidos.grpc_gateway.controller.kafka;

import com.sistemas.distribuidos.grpc_gateway.service.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/hola-mundo")
    public String enviarHolaMundo(@RequestParam(defaultValue = "¡Hola Mundo desde Kafka!") String mensaje) {
        try {
            kafkaProducerService.enviarMensajeHolaMundo(mensaje);
            return "✅ Mensaje enviado exitosamente: " + mensaje;
        } catch (Exception e) {
            return "❌ Error al enviar mensaje: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        String mensaje = "Mensaje de prueba automático - " + System.currentTimeMillis();
        kafkaProducerService.enviarMensajeHolaMundo(mensaje);
        return "✅ Mensaje de prueba enviado: " + mensaje;
    }
}
