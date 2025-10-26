package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class GraphqlResponseDTO<T> {
    private T data;
    private List<Object> errors;
}