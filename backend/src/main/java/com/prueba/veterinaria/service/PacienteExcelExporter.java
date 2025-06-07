package com.prueba.veterinaria.service;

import com.prueba.veterinaria.entity.Paciente;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class PacienteExcelExporter {

    public void export(List<Paciente> lista, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pacientes");

        // Encabezados
        Row row = sheet.createRow(0);
        String[] headers = {
            "ID", "Nombre Mascota", "Especie", "Raza", "Fecha Nacimiento",
            "Tipo ID", "Nro ID", "Nombre Dueño", "Ciudad", "Dirección", "Teléfono", "Fecha Registro"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Datos
        int rowIdx = 1;
        for (Paciente p : lista) {
            Row dataRow = sheet.createRow(rowIdx++);
            dataRow.createCell(0).setCellValue(p.getId());
            dataRow.createCell(1).setCellValue(p.getNombreMascota());
            dataRow.createCell(2).setCellValue(p.getEspecie());
            dataRow.createCell(3).setCellValue(p.getRaza());
            dataRow.createCell(4).setCellValue(p.getFechaNacimiento() != null ? p.getFechaNacimiento().toString() : "");
            dataRow.createCell(5).setCellValue(p.getTipoIdentificacion());
            dataRow.createCell(6).setCellValue(p.getNumeroIdentificacion());
            dataRow.createCell(7).setCellValue(p.getNombreDueno());
            dataRow.createCell(8).setCellValue(p.getCiudad());
            dataRow.createCell(9).setCellValue(p.getDireccion());
            dataRow.createCell(10).setCellValue(p.getTelefono());
            dataRow.createCell(11).setCellValue(p.getFechaRegistro() != null ? p.getFechaRegistro().toString() : "");
        }

        // Descargar
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerValue = "attachment; filename=pacientes.xlsx";
        response.setHeader("Content-Disposition", headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
} 