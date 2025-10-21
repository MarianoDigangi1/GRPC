package com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudesExternasAgrupadas {
    private List<SolicitudDonacionExternaResponseDto> solicitudesPropias;
    private List<SolicitudDonacionExternaResponseDto> solicitudesExternas;
}
