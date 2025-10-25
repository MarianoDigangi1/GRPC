package com.ong.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "filtros_eventos")
@Getter
@Setter
public class FiltroEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Se almacena como JSON en la base de datos.
     * Contiene los parámetros del filtro (fechas, flags, etc.).
     */
    @Column(columnDefinition = "json", nullable = false)
    private String parametros;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}



