package com.ong.kafka_producer.service.consumer.transferencia_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacionItem;
import com.ong.kafka_producer.repository.transferencia_donacion.TransferenciaDonacionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransferenciaDonacionExternaService {

    @Autowired
    private TransferenciaDonacionRepository transferenciaDonacionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    @Transactional
    public void procesarTransferenciaExterna(String mensaje) {
        try {
            TransferenciaDonacionDto transferenciaDto = objectMapper.readValue(mensaje, TransferenciaDonacionDto.class);

            if (esTransferenciaPropia(transferenciaDto, idOrganizacion)) return;
            if (existeTransferenciaEnBDD(transferenciaDto)) return;

            // Crear entity
            TransferenciaDonacion transferenciaExterna = TransferenciaDonacion.builder()
                    .idTransferencia(transferenciaDto.getIdTransferencia())
                    .idOrganizacionOrigen(transferenciaDto.getIdOrganizacionOrigen())
                    .idOrganizacionDestino(transferenciaDto.getIdOrganizacionDestino())
                    .fechaTransferencia(LocalDateTime.now())
                    .build();

            // Crear items
            List<TransferenciaDonacionItem> items = new ArrayList<>();
            transferenciaDto.getDonaciones().forEach(donacion -> {
                TransferenciaDonacionItem item = TransferenciaDonacionItem.builder()
                        .categoria(donacion.getCategoria())
                        .descripcion(donacion.getDescripcion())
                        .transferenciaDonacion(transferenciaExterna)
                        .build();
                items.add(item);
            });

            transferenciaExterna.setItems(items);

            // Guardar en base de datos (ahora dentro de @Transactional)
            transferenciaDonacionRepository.save(transferenciaExterna);

            log.info("Transferencia externa guardada: {}", transferenciaDto.getIdTransferencia());

        } catch (Exception e) {
            log.error("Error al procesar transferencia externa: {}", e.getMessage(), e);
        }
    }

    private boolean existeTransferenciaEnBDD(TransferenciaDonacionDto transferenciaDto) {
        return transferenciaDonacionRepository.findByIdTransferencia(transferenciaDto.getIdTransferencia())
                .map(existing -> {
                    log.info("Transferencia con id {} ya existe", transferenciaDto.getIdTransferencia());
                    return true;
                }).orElse(false);
    }

    private static boolean esTransferenciaPropia(TransferenciaDonacionDto transferenciaDto, Integer idOrganizacion) {
        return transferenciaDto.getIdOrganizacionOrigen().equals(idOrganizacion);
    }
}





