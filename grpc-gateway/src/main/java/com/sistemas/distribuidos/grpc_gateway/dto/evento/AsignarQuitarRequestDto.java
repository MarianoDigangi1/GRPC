package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AsignarQuitarRequestDto {
    private int eventoId;
    private int usuarioId;
    private int actorUsuarioId;
    private String actorRol;
    private boolean agregar;
}
