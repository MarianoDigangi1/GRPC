package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OngDTO {
    private BigInteger id;
    private String name;
    private String address;
    private String phone;
}

