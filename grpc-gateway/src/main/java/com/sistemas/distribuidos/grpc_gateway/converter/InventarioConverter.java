package com.sistemas.distribuidos.grpc_gateway.converter;

import com.sistemas.distribuidos.grpc_gateway.dto.inventario.*;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.*;

import java.util.List;


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

    public static List<InventarioListResponseDto> convertInventarioListResponseToDto(ListarInventarioResponse response) {
        return response.getInventariosList().stream()
                .map(inventario -> InventarioListResponseDto.builder()
                        .id(inventario.getId())
                        .categoria(inventario.getCategoria())
                        .descripcion(inventario.getDescripcion())
                        .cantidad(inventario.getCantidad())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

    public static InventarioDto convertObtenerInventarioPorIdResponseToDto(ObtenerInventarioPorIdResponse response, int id) {
        return InventarioDto.builder()
                .id(id)
                .descripcion(response.getInventario().getDescripcion())
                .cantidad(response.getInventario().getCantidad())
                .categoria(response.getInventario().getCategoria())
                .build();
    }
}
