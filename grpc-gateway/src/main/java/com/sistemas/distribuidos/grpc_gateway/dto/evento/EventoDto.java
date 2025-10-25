package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoDto {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaEventoIso;
    private List<Integer> miembrosIds;
    private boolean publicado;
    private String eventoIdOrganizacionExterna;
    private String miembrosNombres;


}
