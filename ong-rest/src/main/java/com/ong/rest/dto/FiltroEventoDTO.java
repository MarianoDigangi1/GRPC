package com.ong.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroEventoDTO {
    private int id;
    private String nombreFiltro;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private int idUsuario; // usuario que guardó el filtro
    private Boolean repartoDonaciones; // true, false o null si no filtra por esto
}



