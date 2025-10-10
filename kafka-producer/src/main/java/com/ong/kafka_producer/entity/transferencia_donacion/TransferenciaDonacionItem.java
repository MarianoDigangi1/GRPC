package com.ong.kafka_producer.entity.transferencia_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transferencia_donacion_externa_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaDonacionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Integer cantidad = 0; // ahora tenemos cantidad, para que el service funcione

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferencia_id")
    private TransferenciaDonacion transferenciaDonacion;
}
