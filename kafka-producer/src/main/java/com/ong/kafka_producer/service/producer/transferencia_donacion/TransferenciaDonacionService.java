package com.ong.kafka_producer.service.producer.transferencia_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.transferencia_donacion.DonacionDto;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.entity.transferencia_donacion.Inventario;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
import com.ong.kafka_producer.repository.transferencia_donacion.TransferenciaDonacionRepository;
import com.ong.kafka_producer.repository.transferencia_donacion.InventarioRepository;
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
    private final InventarioRepository inventarioRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.transferencia.donaciones}")
    private String transferenciaDonacionesTopic;

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacionDonante;

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
                    .esExterna(false)
                    .build();

            // Crear items
            List<DonacionDto> items = new ArrayList<>();
            if (transferenciaDto.getDonaciones() != null) {
                transferenciaDto.getDonaciones().forEach(donacion -> {
                            Inventario.Categoria categoriaEnum;
                            try {
                                categoriaEnum = Inventario.Categoria.valueOf(donacion.getCategoria().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                log.warn("⚠️ Categoria inválida '{}', se ignora el item: {}", donacion.getCategoria(), donacion.getDescripcion());
                                return; // ignora este item
                            }
                    items.add(donacion);
                });
            }

            String contenidoJson = objectMapper.writeValueAsString(items);
            transferenciaEntity.setContenido(contenidoJson);

            // 🔽 Descontar stock del inventario antes de publicar
            actualizarInventario(items);

            // Guardar en base de datos
            repository.save(transferenciaEntity);

            // Publicar en Kafka
            String mensaje = objectMapper.writeValueAsString(transferenciaDto);
            String topicDestino = transferenciaDonacionesTopic + "-" + transferenciaDto.getIdOrganizacionDestino();
            kafkaTemplate.send(topicDestino, mensaje);
            log.info("📤 Transferencia de donación publicada: {}", idTransferencia);

            return new ResponseDto<>("", true, "Transferencia de donación publicada: " + idTransferencia);

        } catch (Exception e) {
            log.error("❌ Error al transferir donación: {}", e.getMessage(), e);
            return new ResponseDto<>("", false, "Ocurrió un error inesperado al transferir la donación");
        }
    }

    private void actualizarInventario(List<DonacionDto> items) {
        items.forEach(item -> {
            inventarioRepository.findByCategoriaAndDescripcion(Inventario.Categoria.valueOf(item.getCategoria().toUpperCase()), item.getDescripcion())
                    .ifPresentOrElse(
                            inventario -> {
                                int nuevaCantidad = Math.max(0, inventario.getCantidad() - item.getCantidad());
                                inventario.setCantidad(nuevaCantidad);
                                inventario.setUpdatedAt(LocalDateTime.now());
                                //inventario.setUpdatedBy(idOrganizacionDonante); con esto no deberia fallar
                                inventarioRepository.save(inventario);
                                log.info("📉 Inventario reducido: {} -{}", item.getDescripcion(), item.getCantidad());
                            },
                            () -> log.warn("⚠️ No existe inventario para {} - no se pudo descontar.", item.getDescripcion())
                    );
            //TODO: Aca falta retornar los que se van a transferir y existen en inventario. Tambien chequear si la cantidad es negativa
        });
    }
}







