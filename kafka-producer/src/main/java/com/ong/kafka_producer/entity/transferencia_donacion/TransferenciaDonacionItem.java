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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Inventario.Categoria categoria; // ahora es enum

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferencia_id")
    private TransferenciaDonacion transferenciaDonacion;
}




