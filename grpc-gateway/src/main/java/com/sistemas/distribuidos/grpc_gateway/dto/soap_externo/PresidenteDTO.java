package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresidenteDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private Long organizationId;
}
