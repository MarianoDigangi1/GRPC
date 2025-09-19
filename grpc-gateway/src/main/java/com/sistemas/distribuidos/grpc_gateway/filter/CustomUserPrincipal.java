package com.sistemas.distribuidos.grpc_gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Data
@Builder
public class CustomUserPrincipal implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserPrincipal that = (CustomUserPrincipal) o;
        return id == that.id && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
