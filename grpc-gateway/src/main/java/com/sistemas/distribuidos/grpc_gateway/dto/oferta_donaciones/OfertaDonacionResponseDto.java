package com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDonacionResponseDto {
    private Integer id;
    private String externalOrgId;
    private String ofertaId;
    private List<OfertaDonacionItemResponseDto> items;
}
