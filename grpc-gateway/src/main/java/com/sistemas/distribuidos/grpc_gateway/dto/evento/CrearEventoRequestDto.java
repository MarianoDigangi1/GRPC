package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEventoRequestDto {
    private String nombre;
    private String descripcion;
    // TODO: Ver si lo pasamos a localdate
    private String fechaEventoIso;
    private List<Integer> miembrosIds;
    private int actorUsuarioId;
    private String actorRol;
}
