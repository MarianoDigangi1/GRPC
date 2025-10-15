package com.ong.kafka_producer.service.consumer.transferencia_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacionItem;
import com.ong.kafka_producer.entity.transferencia_donacion.Inventario;
import com.ong.kafka_producer.repository.transferencia_donacion.TransferenciaDonacionRepository;
import com.ong.kafka_producer.repository.transferencia_donacion.InventarioRepository;
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
    private InventarioRepository inventarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    @Transactional
    public void procesarTransferenciaExterna(String mensaje) {
        try {
            TransferenciaDonacionDto transferenciaDto = objectMapper.readValue(mensaje, TransferenciaDonacionDto.class);

            // ⚠️ Comparación segura para evitar NullPointerException
            if (esTransferenciaPropia(transferenciaDto, idOrganizacion)) return;
            if (existeTransferenciaEnBDD(transferenciaDto)) return;

            // 🔒 Validar campos obligatorios antes de persistir
            if (transferenciaDto.getIdOrganizacionOrigen() == null ||
                transferenciaDto.getIdOrganizacionDestino() == null ||
                transferenciaDto.getIdTransferencia() == null) {
                log.error("❌ Campos obligatorios nulos en la transferencia: {}", transferenciaDto);
                return;
            }

            // Crear entidad principal
            TransferenciaDonacion transferenciaExterna = TransferenciaDonacion.builder()
                    .idTransferencia(transferenciaDto.getIdTransferencia())
                    .idOrganizacionOrigen(transferenciaDto.getIdOrganizacionOrigen())
                    .idOrganizacionDestino(transferenciaDto.getIdOrganizacionDestino())
                    .fechaTransferencia(LocalDateTime.now())
                    .activa(true)
                    .esExterna(true)
                    .build();

            // Crear items
            List<TransferenciaDonacionItem> items = new ArrayList<>();
            if (transferenciaDto.getDonaciones() != null) {
                transferenciaDto.getDonaciones().forEach(donacion -> {
                    Inventario.Categoria categoriaEnum;
                    try {
                        categoriaEnum = Inventario.Categoria.valueOf(donacion.getCategoria().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        log.warn("⚠️ Categoria inválida '{}', se ignora el item: {}", donacion.getCategoria(), donacion.getDescripcion());
                        return;
                    }

                    TransferenciaDonacionItem item = TransferenciaDonacionItem.builder()
                            .categoria(categoriaEnum)
                            .descripcion(donacion.getDescripcion())
                            .cantidad(donacion.getCantidad())
                            .transferenciaDonacion(transferenciaExterna)
                            .build();
                    items.add(item);
                });
            }

            transferenciaExterna.setItems(items);
            transferenciaDonacionRepository.save(transferenciaExterna);

            // 🔁 Actualizar inventario
            actualizarInventario(items);

            log.info("✅ Transferencia externa guardada y stock actualizado: {}", transferenciaDto.getIdTransferencia());

        } catch (Exception e) {
            log.error("❌ Error al procesar transferencia externa: {}", e.getMessage(), e);
        }
    }

    private boolean existeTransferenciaEnBDD(TransferenciaDonacionDto transferenciaDto) {
        return transferenciaDonacionRepository.findByIdTransferencia(transferenciaDto.getIdTransferencia())
                .map(existing -> {
                    log.info("⚠️ Transferencia con id {} ya existe", transferenciaDto.getIdTransferencia());
                    return true;
                }).orElse(false);
    }

    private static boolean esTransferenciaPropia(TransferenciaDonacionDto transferenciaDto, Integer idOrganizacion) {
        // 🔒 Comparación segura: primero verificamos que el campo no sea null
        Integer origen = transferenciaDto.getIdOrganizacionOrigen();
        return origen != null && origen.equals(idOrganizacion);
    }

    private void actualizarInventario(List<TransferenciaDonacionItem> items) {
        items.forEach(item -> {
            Inventario.Categoria categoriaEnum = item.getCategoria();

            inventarioRepository.findByCategoriaAndDescripcion(categoriaEnum, item.getDescripcion())
                    .ifPresentOrElse(
                            inventario -> {
                                inventario.setCantidad(inventario.getCantidad() + item.getCantidad());
                                inventario.setUpdatedAt(LocalDateTime.now());
                                inventario.setUpdatedBy(idOrganizacion);
                                inventarioRepository.save(inventario);
                                log.info("🔁 Inventario actualizado: {} +{}", item.getDescripcion(), item.getCantidad());
                            },
                            () -> {
                                inventarioRepository.save(
                                        Inventario.builder()
                                                .categoria(categoriaEnum)
                                                .descripcion(item.getDescripcion())
                                                .cantidad(item.getCantidad())
                                                .eliminado(false)
                                                .createdAt(LocalDateTime.now())
                                                .createdBy(idOrganizacion)
                                                .build()
                                );
                                log.info("🆕 Inventario creado: {} ({} unidades)", item.getDescripcion(), item.getCantidad());
                            }
                    );
        });
    }
}





