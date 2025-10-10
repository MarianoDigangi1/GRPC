package com.ong.kafka_producer.service.producer.transferencia_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacionItem;
import com.ong.kafka_producer.repository.transferencia_donacion.TransferenciaDonacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferenciaDonacionService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TransferenciaDonacionRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.transferencia.donaciones}")
    private String transferenciaDonacionesTopic;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacionDonante;

    @Transactional
    public ResponseDto<String> transferirDonacion(TransferenciaDonacionDto transferenciaDto) {
        try {
            // Generar ID de la transferencia
            String idTransferencia = UUID.randomUUID().toString();
            transferenciaDto.setIdTransferencia(idTransferencia);
            transferenciaDto.setIdOrganizacionOrigen(idOrganizacionDonante);

            // Crear entidad principal
            TransferenciaDonacion transferenciaEntity = TransferenciaDonacion.builder()
                    .idTransferencia(idTransferencia)
                    .idOrganizacionOrigen(idOrganizacionDonante)
                    .idOrganizacionDestino(transferenciaDto.getIdOrganizacionDestino())
                    .fechaTransferencia(LocalDateTime.now())
                    .build();

            // Crear items
            List<TransferenciaDonacionItem> items = new ArrayList<>();
            if (transferenciaDto.getDonaciones() != null) {
                transferenciaDto.getDonaciones().forEach(donacion -> {
                    TransferenciaDonacionItem item = TransferenciaDonacionItem.builder()
                            .categoria(donacion.getCategoria())
                            .descripcion(donacion.getDescripcion())
                            .transferenciaDonacion(transferenciaEntity)
                            .build();
                    items.add(item);
                });
            }

            transferenciaEntity.setItems(items);

            // Guardar en base de datos
            repository.save(transferenciaEntity);

            // Publicar en Kafka
            String mensaje = objectMapper.writeValueAsString(transferenciaDto);
            String topicDestino = transferenciaDonacionesTopic + "-" + transferenciaDto.getIdOrganizacionDestino();

            kafkaTemplate.send(topicDestino, mensaje);
            log.info("Transferencia de donación publicada: {}", idTransferencia);

            return new ResponseDto<>("", true, "Transferencia de donación publicada: " + idTransferencia);

        } catch (Exception e) {
            log.error("Error al transferir donación: {}", e.getMessage(), e);
            return new ResponseDto<>("", false, "Ocurrió un error inesperado al transferir la donación");
        }
    }
}





