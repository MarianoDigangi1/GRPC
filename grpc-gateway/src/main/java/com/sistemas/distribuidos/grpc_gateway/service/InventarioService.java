package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.InventarioConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.*;
//import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.*;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<InventarioListResponseDto> listarInventario() {
        try {
            ListarInventarioResponse response = stubBlocking.listarInventario(Empty.newBuilder().build());
            return InventarioConverter.convertInventarioListResponseToDto(response);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public InventarioDto obtenerInventarioPorId(int id) {
        try {
            ObtenerInventarioPorIdResponse resonse = stubBlocking.obtenerInventarioPorId(ObtenerInventarioPorIdRequest.newBuilder().setId(id).build());
            return InventarioConverter.convertObtenerInventarioPorIdResponseToDto(resonse, id);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }


    

}
