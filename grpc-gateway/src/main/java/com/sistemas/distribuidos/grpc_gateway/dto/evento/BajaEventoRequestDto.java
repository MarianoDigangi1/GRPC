package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BajaEventoRequestDto {
    private int id;
    private int actorUsuarioId;
    private String actorRol;
}
