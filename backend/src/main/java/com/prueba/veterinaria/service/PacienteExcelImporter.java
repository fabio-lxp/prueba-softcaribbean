package com.prueba.veterinaria.service;

import com.prueba.veterinaria.entity.Paciente;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PacienteExcelImporter {
    public List<Paciente> importar(MultipartFile file) throws IOException {
        List<Paciente> pacientes = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) { firstRow = false; continue; } // Saltar encabezado
                Paciente p = new Paciente();
                double idValue = getNumericCellValue(row, 0);
                if (idValue > 0) {
                    p.setId((long) idValue);
                }
                p.setNombreMascota(getStringCellValue(row, 1));
                p.setEspecie(getStringCellValue(row, 2));
                p.setRaza(getStringCellValue(row, 3));
                p.setFechaNacimiento(parseLocalDate(getStringCellValue(row, 4)));
                p.setTipoIdentificacion(getStringCellValue(row, 5));
                p.setNumeroIdentificacion(getStringCellValue(row, 6));
                p.setNombreDueno(getStringCellValue(row, 7));
                p.setCiudad(getStringCellValue(row, 8));
                p.setDireccion(getStringCellValue(row, 9));
                p.setTelefono(getStringCellValue(row, 10));
                p.setFechaRegistro(parseLocalDate(getStringCellValue(row, 11)));
                pacientes.add(p);
            }
        }
        return pacientes;
    }

    private String getStringCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        return cell != null ? cell.toString() : "";
    }

    private double getNumericCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try { return Double.parseDouble(cell.toString()); } catch (Exception e) { return 0; }
    }

    private LocalDate parseLocalDate(String value) {
        try { return LocalDate.parse(value); } catch (Exception e) { return null; }
    }
} 