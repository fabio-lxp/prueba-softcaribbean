package com.prueba.veterinaria.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteExcelImporterTest {

    // test para comprobar si el archivo que importa el usuario es un archivo de tipo xlsx.
    @Test
    void importarLanzaExcepcionSiNoEsExcel() throws IOException {
        PacienteExcelImporter importer = new PacienteExcelImporter();
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("archivo.doc");
        when(file.getInputStream()).thenThrow(new IOException("No debería llegar aquí"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> importer.importar(file));
        assertEquals("Solo se permiten archivos Excel (.xlsx)", ex.getMessage());
    }

    @Test
    void parseLocalDateDevuelveNullSiFormatoIncorrecto() throws Exception {
        PacienteExcelImporter importer = new PacienteExcelImporter();
        Method method = PacienteExcelImporter.class.getDeclaredMethod("parseLocalDate", String.class);
        method.setAccessible(true);
        assertNull(method.invoke(importer, "no-es-fecha"));
        assertEquals(LocalDate.of(2024, 6, 7), method.invoke(importer, "2024-06-07"));
    }

    // este test revisa que al importar un paciente con datos en blanco quedan ("") para strings o null para fechas

    @Test
    void importarArchivoConCeldasVacias() throws Exception {

        // Crear un Excel en memoria con una fila de datos y algunas celdas vacías
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row header = sheet.createRow(0);
        for (int i = 0; i < 12; i++) header.createCell(i).setCellValue("Col" + i);
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(1); // ID
        row.createCell(1).setCellValue(""); // nombreMascota vacío
        row.createCell(2).setCellValue("Perro"); // especie
        row.createCell(3).setCellValue(""); // raza vacío
        row.createCell(4).setCellValue(""); // fechaNacimiento vacío
        row.createCell(5).setCellValue("CC"); // tipoIdentificacion
        row.createCell(6).setCellValue(""); // numeroIdentificacion vacío
        row.createCell(7).setCellValue("Juan"); // nombreDueno
        row.createCell(8).setCellValue(""); // ciudad vacío
        row.createCell(9).setCellValue("Calle 1"); // direccion
        row.createCell(10).setCellValue(""); // telefono vacío
        row.createCell(11).setCellValue(""); // fechaRegistro vacío

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.xlsx");
        when(file.getInputStream()).thenReturn(bais);

        PacienteExcelImporter importer = new PacienteExcelImporter();
        List<com.prueba.veterinaria.entity.Paciente> pacientes = importer.importar(file);
        assertEquals(1, pacientes.size());
        com.prueba.veterinaria.entity.Paciente p = pacientes.get(0);
        assertEquals("", p.getNombreMascota());
        assertEquals("Perro", p.getEspecie());
        assertEquals("", p.getRaza());
        assertNull(p.getFechaNacimiento());
        assertEquals("CC", p.getTipoIdentificacion());
        assertEquals("", p.getNumeroIdentificacion());
        assertEquals("Juan", p.getNombreDueno());
        assertEquals("", p.getCiudad());
        assertEquals("Calle 1", p.getDireccion());
        assertEquals("", p.getTelefono());
        assertNull(p.getFechaRegistro());
    }
} 