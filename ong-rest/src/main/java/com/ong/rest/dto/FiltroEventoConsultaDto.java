package com.ong.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroEventoConsultaDto {
    private LocalDate fechaDesde; // opcional
    private LocalDate fechaHasta; // opcional
    private String estado; // "vigentes", "no vigentes", "todos"
}

