package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.FiltroEventoDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FiltroEventoService {

    private final WebClient webClient;

    public FiltroEventoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/filtros-eventos").build();
    }

    // =========================
    // CREAR FILTRO (POST)
    // =========================
    public FiltroEventoDto crearFiltro(FiltroEventoDto filtroEventoDto) throws GrpcConnectionException {
        try {
            return webClient.post()
                    .uri("/usuario/{usuarioId}", filtroEventoDto.getUsuarioId())
                    .bodyValue(filtroEventoDto)
                    .retrieve()
                    .bodyToMono(FiltroEventoDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new GrpcConnectionException("Error del backend: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new GrpcConnectionException("No se pudo conectar con el backend: " + e.getMessage());
        }
    }

    // =========================
    // OBTENER FILTROS POR USUARIO (GET)
    // =========================
    public List<FiltroEventoDto> obtenerFiltrosPorUsuario(int usuarioId) throws GrpcConnectionException {
        try {
            FiltroEventoDto[] filtros = webClient.get()
                    .uri("/usuario/{usuarioId}", usuarioId)
                    .retrieve()
                    .bodyToMono(FiltroEventoDto[].class)
                    .block();

            return filtros != null ? Arrays.asList(filtros) : Collections.emptyList();
        } catch (WebClientResponseException e) {
            throw new GrpcConnectionException("Error del backend: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new GrpcConnectionException("No se pudo conectar con el backend: " + e.getMessage());
        }
    }

    // =========================
    // OBTENER FILTRO POR ID (GET)
    // =========================
    public FiltroEventoDto obtenerFiltroPorId(int id) throws GrpcConnectionException {
        try {
            Map<String, Object> response = webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) return null;

            FiltroEventoDto dto = new FiltroEventoDto();
            dto.setId((Integer) response.get("id"));
            dto.setNombre((String) response.get("nombre"));
            dto.setParametros((String) response.get("parametros"));

            // Mapear usuarioId correctamente
            Map<String, Object> usuarioMap = (Map<String, Object>) response.get("usuario");
            if (usuarioMap != null && usuarioMap.get("id") != null) {
                Object usuarioIdObj = usuarioMap.get("id");
                if (usuarioIdObj instanceof Integer) {
                    dto.setUsuarioId((Integer) usuarioIdObj);
                } else if (usuarioIdObj instanceof Number) {
                    dto.setUsuarioId(((Number) usuarioIdObj).intValue());
                } else {
                    dto.setUsuarioId(0); // fallback
                }
            }

            return dto;
        } catch (WebClientResponseException e) {
            throw new GrpcConnectionException("Error del backend: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new GrpcConnectionException("No se pudo conectar con el backend: " + e.getMessage());
        }
    }

    // =========================
    // ACTUALIZAR FILTRO (PUT)
    // =========================
    public FiltroEventoDto actualizarFiltro(int id, FiltroEventoDto filtroEventoDto) throws GrpcConnectionException {
        try {
            return webClient.put()
                    .uri("/{id}", id)
                    .bodyValue(filtroEventoDto)
                    .retrieve()
                    .bodyToMono(FiltroEventoDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new GrpcConnectionException("Error del backend: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new GrpcConnectionException("No se pudo conectar con el backend: " + e.getMessage());
        }
    }

    // =========================
    // ELIMINAR FILTRO (DELETE)
    // =========================
    public void eliminarFiltro(int id) throws GrpcConnectionException {
        try {
            webClient.delete()
                    .uri("/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() != HttpStatus.NO_CONTENT) {
                throw new GrpcConnectionException("Error del backend: " + e.getResponseBodyAsString());
            }
        } catch (Exception e) {
            throw new GrpcConnectionException("No se pudo conectar con el backend: " + e.getMessage());
        }
    }
}










