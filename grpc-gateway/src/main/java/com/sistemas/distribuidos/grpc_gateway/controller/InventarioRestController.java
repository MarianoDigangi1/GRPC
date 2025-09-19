package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.BajaInventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.ModificarInventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioRestController {

    private final InventarioService inventarioService;

    @Autowired
    public InventarioRestController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<InventarioResponseDto> registrarInventario(@RequestBody InventarioRequestDto request,
                                                                     Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        InventarioResponseDto response = inventarioService.registrarInventario(request, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modificar")
    public ResponseEntity<InventarioResponseDto> modificarInventario(@RequestBody ModificarInventarioRequestDto request,
                                                                     Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        InventarioResponseDto response = inventarioService.modificarInventario(request, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<InventarioResponseDto> eliminarInventario(@RequestBody BajaInventarioRequestDto request,
                                                                    Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        InventarioResponseDto response = inventarioService.eliminarInventario(request, user);
        return ResponseEntity.ok(response);
    }

}
