package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.InventarioConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.*;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.stubs.inventario.*;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            InventarioResponse inventarioResponse = stubBlocking.registrarInventario(
                    InventarioConverter.convertInventarioRequestFromDto(dto, user)
            );
            return InventarioConverter.convertInventarioResponseToDto(inventarioResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public InventarioResponseDto modificarInventario(ModificarInventarioRequestDto dto, CustomUserPrincipal user) {
        try {
            InventarioResponse inventarioResponse = stubBlocking.modificarInventario(
                    InventarioConverter.convertModificarInventarioRequestFromDto(dto, user)
            );
            return InventarioConverter.convertInventarioResponseToDto(inventarioResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public InventarioResponseDto eliminarInventario(BajaInventarioRequestDto dto, CustomUserPrincipal user) {
        try {
            InventarioResponse inventarioResponse = stubBlocking.bajaInventario(
                    BajaInventarioRequest.newBuilder()
                            .setId(dto.getId())
                            .setUsuarioModificacion(Integer.toString(user.getId()))
                            .build()
            );
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
            ObtenerInventarioPorIdResponse response = stubBlocking.obtenerInventarioPorId(
                    ObtenerInventarioPorIdRequest.newBuilder().setId(id).build()
            );
            return InventarioConverter.convertObtenerInventarioPorIdResponseToDto(response, id);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    // ==========================
    // DESCARGA DE INVENTARIO EXCEL
    // ==========================
    public byte[] descargarInventarioExcel(CustomUserPrincipal user) {
        try {
            // URL del servicio REST del otro proyecto (ong-rest)
            String url = "http://localhost:8081/inventario/excel";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            headers.set("Authorization", "Bearer " + user.getId()); // reemplazamos getToken() por getId()

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new GrpcConnectionException("Error al descargar el Excel, código: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con el servicio de Excel: " + e.getMessage(), e);
        }
    }
}


