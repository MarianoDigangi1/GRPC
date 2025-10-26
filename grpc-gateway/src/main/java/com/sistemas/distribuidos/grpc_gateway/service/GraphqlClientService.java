package com.sistemas.distribuidos.grpc_gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemas.distribuidos.grpc_gateway.dto.graphql.ReporteDonacionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphqlClientService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GraphqlClientService(@Value("${graphql.service.url}") String graphqlServerUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(graphqlServerUrl)
                .build();
    }

    /**
     * Consulta los informes de donaciones agrupados por categoría.
     */
    public Mono<List<ReporteDonacionDTO>> getInformeDonaciones(String categoria, String fechaInicio, String fechaFin, String eliminado) {

        String query = """
                query($categoria: String, $fechaInicio: String, $fechaFin: String, $eliminado: String) {
                    informeDonaciones(categoria: $categoria, fechaInicio: $fechaInicio, fechaFin: $fechaFin, eliminado: $eliminado){
                        categoria
                        totalCantidad
                        eliminado
                    }
                }
                """;

        Map<String, Object> variables = new HashMap<>();
        variables.put("categoria", (categoria != null && !categoria.isBlank()) ? categoria : null);
        variables.put("fechaInicio", (fechaInicio != null && !fechaInicio.isBlank()) ? fechaInicio : null);
        variables.put("fechaFin", (fechaFin != null && !fechaFin.isBlank()) ? fechaFin : null);
        variables.put("eliminado", (eliminado != null && !eliminado.isBlank()) ? eliminado : null);

        return webClient.post()
                .uri("/graphql")
                .bodyValue(Map.of("query", query, "variables", variables))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>)
                        ((Map<String, Object>) response.get("data")).get("informeDonaciones"))
                .map(list -> list.stream()
                        .map(item -> {
                            ReporteDonacionDTO dto = new ReporteDonacionDTO();
                            dto.setCategoria((String) item.get("categoria"));
                            dto.setTotalCantidad(((Number) item.get("totalCantidad")).longValue());
                            dto.setEliminado((Boolean) item.get("eliminado"));
                            return dto;
                        })
                        .toList());
    }

    /**
     * Guarda un filtro personalizado de informes de donaciones.
     * El user_id se envía en el header "X-User-Id" según el backend Flask.
     */
    public Mono<Void> guardarFiltro(long usuarioId, String nombre, String categoria, String fechaInicio, String fechaFin, Boolean eliminado) {

        String mutation = """
            mutation($nombre: String!, $filtros: JSONString!) {
                crearFiltro(
                    filtroData: {
                        nombre: $nombre,
                        filtros: $filtros
                    }
                ) {
                    filtro {
                        id
                        nombre
                    }
                }
            }
            """;

        // Construye el JSON interno del campo "filtros"
        Map<String, Object> filtrosMap = new HashMap<>();
        filtrosMap.put("categoria", categoria);
        filtrosMap.put("fechaInicio", fechaInicio);
        filtrosMap.put("fechaFin", fechaFin);
        filtrosMap.put("eliminado", eliminado);

        String filtrosJson;
        try {
            filtrosJson = objectMapper.writeValueAsString(filtrosMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir filtros a JSON", e);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("nombre", nombre);
        variables.put("filtros", filtrosJson);

        return webClient.post()
                .uri("/graphql")
                .header("X-User-Id", String.valueOf(usuarioId)) // 👈 Importante
                .bodyValue(Map.of("query", mutation, "variables", variables))
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Obtiene todos los filtros guardados por el usuario autenticado.
     */
    public Mono<List<Map<String, Object>>> getMisFiltros(long usuarioId) {
        String query = """
            query {
                misFiltros {
                    id
                    nombre
                    filtros
                }
            }
            """;

        return webClient.post()
                .uri("/graphql")
                .header("X-User-Id", String.valueOf(usuarioId)) // 👈 el backend Flask espera este header
                .bodyValue(Map.of("query", query))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>)
                        ((Map<String, Object>) response.get("data")).get("misFiltros"));
    }
}
