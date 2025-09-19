package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarEventosResponseDto {
    private List<EventoDto> eventos;
}
