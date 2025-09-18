package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModificarEventoRequestDto {
    private int id;
    private String nombre;
    private String fechaEventoIso;
    private List<Integer> agregarMiembrosIds;
    private List<Integer> quitarMiembrosIds;
    private List<DonacionUsadaDto> donacionesUsadas;
    private int actorUsuarioId;
    private String actorRol;
}
