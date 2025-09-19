package com.sistemas.distribuidos.grpc_gateway.converter;

import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.ModificarInventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.InventarioRequest;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.InventarioResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.ModificarInventarioRequest;


public class InventarioConverter {

    public static InventarioRequest convertInventarioRequestFromDto(InventarioRequestDto dto, CustomUserPrincipal user) {
        return InventarioRequest.newBuilder()
                .setCategoria(dto.getCategoria())
                .setDescripcion(dto.getDescripcion())
                .setCantidad(dto.getCantidad())
                .setUsuarioAlta(Integer.toString(user.getId()))
                .build();
    }

    public static InventarioResponseDto convertInventarioResponseToDto(InventarioResponse inventarioResponse) {
        return InventarioResponseDto.builder()
                .success(inventarioResponse.getSuccess())
                .message(inventarioResponse.getMessage())
                .id(inventarioResponse.getId())
                .build();
    }

    public static ModificarInventarioRequest convertModificarInventarioRequestFromDto(ModificarInventarioRequestDto dto, CustomUserPrincipal user) {
        return ModificarInventarioRequest.newBuilder()
                .setId(dto.getId())
                .setDescripcion(dto.getDescripcion())
                .setCantidad(dto.getCantidad())
                .setUsuarioModificacion(Integer.toString(user.getId()))
                .build();
    }
}
