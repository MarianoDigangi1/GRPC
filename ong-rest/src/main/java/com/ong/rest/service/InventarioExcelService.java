package com.ong.rest.service;

import com.ong.rest.dto.InventarioDTO;
import com.ong.rest.entity.Inventario;
import com.ong.rest.entity.Usuario;
import com.ong.rest.repository.InventarioRepository;
import com.ong.rest.repository.UsuarioRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventarioExcelService {

    private final InventarioRepository inventarioRepository;
    private final UsuarioRepository usuarioRepository;

    public InventarioExcelService(InventarioRepository inventarioRepository, UsuarioRepository usuarioRepository) {
        this.inventarioRepository = inventarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método principal: genera Excel con todos los registros de inventario
     */
    public byte[] generarExcel() throws IOException {
        List<Inventario> inventarios = inventarioRepository.findAll();

        // Convertir a DTO
        List<InventarioDTO> dtos = inventarios.stream().map(i -> {
            InventarioDTO dto = new InventarioDTO();
            dto.setId(i.getId());
            dto.setFechaAlta(i.getCreatedAt());
            dto.setDescripcion(i.getDescripcion());
            dto.setCantidad(i.getCantidad() != null ? i.getCantidad() : 0);
            dto.setEliminado(i.getEliminado() != null && i.getEliminado());

            // Buscar nombre del usuario por ID
            String nombreAlta = "Desconocido";
            if (i.getCreatedById() != null) {
                Optional<Usuario> u = usuarioRepository.findById(i.getCreatedById());
                if (u.isPresent()) nombreAlta = u.get().getNombreUsuario();
            }
            dto.setUsuarioAlta(nombreAlta);

            String nombreModificacion = null;
            if (i.getUpdatedById() != null) {
                Optional<Usuario> u = usuarioRepository.findById(i.getUpdatedById());
                if (u.isPresent()) nombreModificacion = u.get().getNombreUsuario();
            }
            dto.setUsuarioModificacion(nombreModificacion);

            dto.setCategoria(i.getCategoria() != null ? i.getCategoria().name() : "Sin Categoría");
            return dto;
        }).collect(Collectors.toList());

        return generarExcel(dtos);
    }

    /**
     * Método que genera el Excel a partir de la lista de DTOs
     */
    private byte[] generarExcel(List<InventarioDTO> dtos) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Agrupar por categoría
        Map<String, List<InventarioDTO>> porCategoria = dtos.stream()
                .collect(Collectors.groupingBy(InventarioDTO::getCategoria));

        for (Map.Entry<String, List<InventarioDTO>> entry : porCategoria.entrySet()) {
            String categoria = entry.getKey();
            Sheet sheet = workbook.createSheet(categoria);

            // Header
            Row header = sheet.createRow(0);
            String[] columnas = {"Fecha de Alta", "Descripción", "Cantidad", "Eliminado", "Usuario Alta", "Usuario Modificación"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);

                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Filas de datos
            int fila = 1;
            for (InventarioDTO d : entry.getValue()) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(d.getFechaAlta() != null ? d.getFechaAlta().toString() : "");
                row.createCell(1).setCellValue(d.getDescripcion() != null ? d.getDescripcion() : "");
                row.createCell(2).setCellValue(d.getCantidad());
                row.createCell(3).setCellValue(d.isEliminado() ? "Sí" : "No");
                row.createCell(4).setCellValue(d.getUsuarioAlta() != null ? d.getUsuarioAlta() : "");
                row.createCell(5).setCellValue(d.getUsuarioModificacion() != null ? d.getUsuarioModificacion() : "");
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}






