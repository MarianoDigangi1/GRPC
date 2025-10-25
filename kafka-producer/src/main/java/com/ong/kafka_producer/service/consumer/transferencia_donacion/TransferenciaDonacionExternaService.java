package com.ong.kafka_producer.service.consumer.transferencia_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.transferencia_donacion.DonacionDto;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
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
    private String idOrganizacion;

    @Transactional
    public void procesarTransferenciaExterna(String mensaje) {
        try {
            TransferenciaDonacionDto transferenciaDto = objectMapper.readValue(mensaje, TransferenciaDonacionDto.class);

            // Evitar procesar transferencias propias
            if (esTransferenciaPropia(transferenciaDto, idOrganizacion)) {
                log.info("⏩ Ignorando transferencia propia: {}", transferenciaDto.getIdTransferencia());
                return;
            }

            // Evitar duplicados
            if (existeTransferenciaEnBDD(transferenciaDto)) {
                log.info("⏩ Transferencia {} ya fue procesada. Se ignora.", transferenciaDto.getIdTransferencia());
                return;
            }

            // Validar campos obligatorios
            if (transferenciaDto.getIdOrganizacionOrigen() == null ||
                transferenciaDto.getIdOrganizacionDestino() == null ||
                transferenciaDto.getIdTransferencia() == null) {
                log.error("❌ Campos obligatorios nulos en la transferencia: {}", transferenciaDto);
                return;
            }

            String contenidoJson = objectMapper.writeValueAsString(transferenciaDto.getDonaciones());

            // Crear entidad principal
            TransferenciaDonacion transferenciaExterna = TransferenciaDonacion.builder()
                    .idTransferencia(transferenciaDto.getIdTransferencia())
                    .idOrganizacionOrigen(transferenciaDto.getIdOrganizacionOrigen())
                    .idOrganizacionDestino(transferenciaDto.getIdOrganizacionDestino())
                    .fechaTransferencia(LocalDateTime.now())
                    .contenido(contenidoJson)
                    .esExterna(true)
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
                        return; // se ignora el item
                    }

//                    TransferenciaDonacionItem item = TransferenciaDonacionItem.builder()
//                            .categoria(categoriaEnum)
//                            .descripcion(donacion.getDescripcion())
//                            .cantidad(donacion.getCantidad())
//                            .transferenciaDonacion(transferenciaExterna)
//                            .build();
                    items.add(donacion);
                });
            }

            //transferenciaExterna.setItems(items);
            transferenciaDonacionRepository.save(transferenciaExterna);
            log.info("✅ Transferencia externa guardada: {}", transferenciaDto.getIdTransferencia());

            // Actualizar inventario si somos el destino
            if (transferenciaDto.getIdOrganizacionDestino().equals(idOrganizacion)) {
                if (!items.isEmpty()) {
                    actualizarInventario(items, true); // sumar stock
                    log.info("🔁 Inventario actualizado para {} items de la transferencia {}", items.size(), transferenciaDto.getIdTransferencia());
                } else {
                    log.warn("⚠️ No hay items válidos para actualizar en inventario de la transferencia {}", transferenciaDto.getIdTransferencia());
                }
            } else {
                log.info("⏩ Transferencia no es hacia nuestra organización (Destino: {}), no se actualiza inventario", transferenciaDto.getIdOrganizacionDestino());
            }

        } catch (Exception e) {
            log.error("❌ Error al procesar transferencia externa: {}", e.getMessage(), e);
        }
    }

    private boolean existeTransferenciaEnBDD(TransferenciaDonacionDto transferenciaDto) {
        return transferenciaDonacionRepository.findByIdTransferencia(transferenciaDto.getIdTransferencia())
                .map(existing -> true).orElse(false);
    }

    private static boolean esTransferenciaPropia(TransferenciaDonacionDto transferenciaDto, String idOrganizacion) {
        String origen = transferenciaDto.getIdOrganizacionOrigen();
        return origen != null && origen.equals(idOrganizacion);
    }

    private void actualizarInventario(List<DonacionDto> items, boolean sumar) {
        items.forEach(item -> {
            Inventario.Categoria categoriaEnum = Inventario.Categoria.valueOf(item.getCategoria());

            inventarioRepository.findByCategoriaAndDescripcion(categoriaEnum, item.getDescripcion())
                    .ifPresentOrElse(
                            inventario -> {
                                int nuevaCantidad = sumar
                                        ? inventario.getCantidad() + item.getCantidad()
                                        : Math.max(0, inventario.getCantidad() - item.getCantidad());
                                inventario.setCantidad(nuevaCantidad);
                                inventario.setUpdatedAt(LocalDateTime.now());
                                //inventario.setUpdatedBy(idOrganizacion); Esto no deberia romper
                                inventarioRepository.save(inventario);
                                log.info("🔁 Inventario actualizado: {} {}{}", item.getDescripcion(), sumar ? "+" : "-", item.getCantidad());
                            },
                            () -> {
                                if (sumar) {
                                    inventarioRepository.save(
                                            Inventario.builder()
                                                    .categoria(categoriaEnum)
                                                    .descripcion(item.getDescripcion())
                                                    .cantidad(item.getCantidad())
                                                    .createdAt(LocalDateTime.now())
                                                    //.createdBy(idOrganizacion) esto no deberia romper
                                                    .eliminado(false)
                                                    .build()
                                    );
                                    log.info("🆕 Inventario creado: {} ({} unidades)", item.getDescripcion(), item.getCantidad());
                                }
                            }
                    );
        });
    }
}








