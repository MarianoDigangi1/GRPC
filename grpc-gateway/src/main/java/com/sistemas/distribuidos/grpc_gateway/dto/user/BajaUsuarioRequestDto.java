package com.sistemas.distribuidos.grpc_gateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BajaUsuarioRequestDto {
    private int id;
}
