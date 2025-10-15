package com.ong.kafka_producer.entity.transferencia_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transferencia_donacion_externa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaDonacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_transferencia", nullable = false)
    private String idTransferencia;

    @Column(name = "id_organizacion_origen", nullable = false)
    private Integer idOrganizacionOrigen;

    @Column(name = "id_organizacion_destino", nullable = false)
    private Integer idOrganizacionDestino;

    @Column(name = "fecha_transferencia")
    private LocalDateTime fechaTransferencia;

    @Column(name = "activa")
    @Builder.Default
    private Boolean activa = true;

    @Column(name = "es_externa", nullable = false)
    @Builder.Default
    private Boolean esExterna = true;

    @OneToMany(mappedBy = "transferenciaDonacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransferenciaDonacionItem> items = new ArrayList<>();
}


