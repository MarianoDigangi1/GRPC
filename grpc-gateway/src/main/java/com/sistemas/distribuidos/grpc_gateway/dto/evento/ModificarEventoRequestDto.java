package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModificarEventoRequestDto {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaEventoIso;
    private List<Integer> agregarMiembrosIds;
    private List<Integer> quitarMiembrosIds;
    private List<DonacionUsadaDto> donacionesUsadas;
    private int actorUsuarioId;
    private String actorRol;
}
