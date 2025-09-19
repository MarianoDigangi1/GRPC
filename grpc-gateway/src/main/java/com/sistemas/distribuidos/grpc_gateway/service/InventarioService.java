package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.InventarioConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.BajaInventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.ModificarInventarioRequestDto;
//import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.BajaInventarioRequest;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.InventarioResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.InventarioServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private final InventarioServiceGrpc.InventarioServiceBlockingStub stubBlocking;

    @Autowired
    public InventarioService(ManagedChannel grpcChannel) {
        this.stubBlocking = InventarioServiceGrpc.newBlockingStub(grpcChannel);
    }

    public InventarioResponseDto registrarInventario(InventarioRequestDto dto, CustomUserPrincipal user) {
        try {
            InventarioResponse inventarioResponse = stubBlocking.registrarInventario(InventarioConverter.convertInventarioRequestFromDto(dto, user));
            return InventarioConverter.convertInventarioResponseToDto(inventarioResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public InventarioResponseDto modificarInventario(ModificarInventarioRequestDto dto, CustomUserPrincipal user) {
        try {
            InventarioResponse inventarioResponse = stubBlocking.modificarInventario(InventarioConverter.convertModificarInventarioRequestFromDto(dto, user));
            return InventarioConverter.convertInventarioResponseToDto(inventarioResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public InventarioResponseDto eliminarInventario(BajaInventarioRequestDto dto, CustomUserPrincipal user) {
        try {
            InventarioResponse inventarioResponse = stubBlocking.bajaInventario(BajaInventarioRequest.newBuilder().setId(dto.getId()).setUsuarioModificacion(Integer.toString(user.getId())).build());
            return InventarioConverter.convertInventarioResponseToDto(inventarioResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }


}
