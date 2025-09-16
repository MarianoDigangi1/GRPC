package com.sistemas.distribuidos.grpc_gateway.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private String error;
    private String detail;

    public ErrorResponseDto(String error, String detail) {
        this.error = error;
        this.detail = detail;
    }
}