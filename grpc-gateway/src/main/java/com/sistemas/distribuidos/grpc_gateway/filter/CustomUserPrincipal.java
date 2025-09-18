package com.sistemas.distribuidos.grpc_gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomUserPrincipal {
    private int id;
    private String username;
    private String role;
}
