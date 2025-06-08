package com.prueba.veterinaria.service;

import com.prueba.veterinaria.entity.Paciente;
import org.junit.jupiter.api.Test;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteExcelExporterTest {

    // este test sirve para probar listas vacias

    @Test
    void exportNoLanzaExcepcionConListaVacia() throws IOException {
        PacienteExcelExporter exporter = new PacienteExcelExporter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // No hace nada para este test
            }
        };
        when(response.getOutputStream()).thenReturn(sos);
        assertDoesNotThrow(() -> exporter.export(Collections.emptyList(), response));
    }

    // este test sirve para exportar una lista con un paciente válido

    @Test
    void exportNoLanzaExcepcionConUnPacienteValido() throws IOException {
        PacienteExcelExporter exporter = new PacienteExcelExporter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {}
        };
        when(response.getOutputStream()).thenReturn(sos);

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombreMascota("Firulais");
        paciente.setEspecie("Perro");
        paciente.setRaza("Beagle");
        paciente.setFechaNacimiento(java.time.LocalDate.of(2020, 1, 1));
        paciente.setTipoIdentificacion("CC");
        paciente.setNumeroIdentificacion("123");
        paciente.setNombreDueno("Juan");
        paciente.setCiudad("Bogotá");
        paciente.setDireccion("Calle 1");
        paciente.setTelefono("123456789");
        paciente.setFechaRegistro(java.time.LocalDate.of(2024, 6, 7));

        assertDoesNotThrow(() -> exporter.export(Collections.singletonList(paciente), response));
        assertTrue(baos.size() > 0);
    }

    // Exportar una lista con un paciente con campos vacíos o nulos

    @Test
    void exportNoLanzaExcepcionConPacienteConCamposVaciosONulos() throws IOException {
        PacienteExcelExporter exporter = new PacienteExcelExporter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {}
        };
        when(response.getOutputStream()).thenReturn(sos);

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombreMascota("");
        paciente.setEspecie("");
        paciente.setRaza("");
        paciente.setTipoIdentificacion("");
        paciente.setNumeroIdentificacion("");
        paciente.setNombreDueno("");
        paciente.setCiudad("");
        paciente.setDireccion("");
        paciente.setTelefono("");
        // fechas en null

        assertDoesNotThrow(() -> exporter.export(Collections.singletonList(paciente), response));
        assertTrue(baos.size() > 0);
    }
} 