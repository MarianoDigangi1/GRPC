package com.ong.rest.controller;

import com.ong.rest.service.InventarioExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(
        name = "Descarga excel",
        description = "Descargas"
)
@RestController
@RequestMapping("/inventario")
public class InventarioExcelController {

    @Autowired
    private InventarioExcelService excelService;

    @Operation(summary = "Descarga informe de donaciones", description = "Retorna un archivo en formato .xlsx con el informe con el registro del inventario de las donaciones")
    @GetMapping(value = "/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> descargarExcel() throws IOException {
        // Usamos el servicio que ya conecta con la BD
        byte[] excelBytes = excelService.generarExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventario.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}




