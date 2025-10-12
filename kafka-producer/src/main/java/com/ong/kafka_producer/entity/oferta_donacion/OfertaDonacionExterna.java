package com.ong.kafka_producer.entity.oferta_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "oferta_donacion_externa")
public class OfertaDonacionExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_oferta", nullable = false, length = 100)
    private String idOferta;

    @Column(name = "id_organizacion_donante", nullable = false)
    private Integer idOrganizacionDonante;


}
