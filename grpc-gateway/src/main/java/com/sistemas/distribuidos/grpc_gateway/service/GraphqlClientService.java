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
            .map(response -> (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("informeDonaciones"))
            .map(list -> list.stream().map(item -> {
                ReporteDonacionDTO dto = new ReporteDonacionDTO();
                dto.setCategoria((String) item.get("categoria"));
                dto.setTotalCantidad(((Number) item.get("totalCantidad")).longValue());
                dto.setEliminado((Boolean) item.get("eliminado"));
                return dto;
            }).toList());
}

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
            .header("X-User-Id", String.valueOf(usuarioId))
            .bodyValue(Map.of("query", query))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                List<Map<String, Object>> filtros = (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("misFiltros");
                return filtros.stream().map(f -> {
                    String filtrosJson = (String) f.get("filtros");
                    f.put("filtrosJson", filtrosJson);
                    try {
                        Map<String, Object> map = objectMapper.readValue(filtrosJson, Map.class);
                        String categoria = map.get("categoria") != null ? map.get("categoria").toString() : "-";
                        String eliminado = map.get("eliminado") == null ? "Ambos" : (Boolean.TRUE.equals(map.get("eliminado")) ? "Sí" : "No");
                        String fechaInicio = map.get("fechaInicio") != null && !map.get("fechaInicio").toString().isBlank() ? map.get("fechaInicio").toString() : "-";
                        String fechaFin = map.get("fechaFin") != null && !map.get("fechaFin").toString().isBlank() ? map.get("fechaFin").toString() : "-";
                        String detalles = String.format("Categoría: %s | Eliminado: %s | Desde: %s | Hasta: %s", categoria, eliminado, fechaInicio, fechaFin);
                        f.put("filtros", detalles);
                    } catch (Exception e) {
                        f.put("filtros", filtrosJson);
                    }
                    return f;
                }).toList();
            });
}

public Mono<Void> guardarFiltro(long usuarioId, String nombre, String categoria, String fechaInicio, String fechaFin, Boolean eliminado) {
    String mutation = """
        mutation($nombre: String!, $filtros: JSONString!) {
          crearFiltro(filtroData: { nombre: $nombre, filtros: $filtros }) { filtro { id nombre } }
        }
        """;
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
            .header("X-User-Id", String.valueOf(usuarioId))
            .bodyValue(Map.of("query", mutation, "variables", variables))
            .retrieve()
            .bodyToMono(Map.class)
            .then();
}

public Mono<List<Map<String, Object>>> getInformeParticipacion(int usuarioId, String fechaInicio, String fechaFin) {
    String query = """
        query($usuarioId: Int!, $fechaInicio: String, $fechaFin: String) {
          informeParticipacion(usuarioId: $usuarioId, fechaInicio: $fechaInicio, fechaFin: $fechaFin) {
            mes
            eventos { dia nombre descripcion }
          }
        }
        """;
    Map<String, Object> variables = new HashMap<>();
    variables.put("usuarioId", usuarioId);
    variables.put("fechaInicio", fechaInicio);
    variables.put("fechaFin", fechaFin);
    return webClient.post()
            .uri("/graphql")
            .bodyValue(Map.of("query", query, "variables", variables))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("informeParticipacion"));
}

public Mono<Void> eliminarFiltro(long userId, long filtroId) {
    String mutation = """
        mutation($id: ID!) { eliminarFiltro(id: $id) { ok } }
        """;
    return webClient.post()
            .uri("/graphql")
            .header("X-User-Id", String.valueOf(userId))
            .bodyValue(Map.of("query", mutation, "variables", Map.of("id", String.valueOf(filtroId))))
            .retrieve()
            .bodyToMono(Map.class)
            .then();
}

public Mono<Void> editarFiltro(long userId, long filtroId, String nuevoNombre) {
    String mutation = """
        mutation($id: ID!, $filtro: JSONString!, $nombre: String!) {
          actualizarFiltro(id: $id, filtroData: { nombre: $nombre, filtros: $filtro }) { ok }
        }
        """;
    String filtroVacio = "{}";
    return webClient.post()
            .uri("/graphql")
            .header("X-User-Id", String.valueOf(userId))
            .bodyValue(Map.of("query", mutation, "variables", Map.of("id", String.valueOf(filtroId), "filtro", filtroVacio, "nombre", nuevoNombre)))
            .retrieve()
            .bodyToMono(Map.class)
            .then();
}



}
