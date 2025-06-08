package com.prueba.veterinaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.veterinaria.entity.Paciente;
import com.prueba.veterinaria.repository.PacienteRepository;
import com.prueba.veterinaria.service.PacienteExcelExporter;
import com.prueba.veterinaria.service.PacienteExcelImporter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@WithMockUser(username = "admin", roles = "USER") // Simula login
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteRepository repository;

    @MockBean
    private PacienteExcelExporter excelExporter;

    @MockBean
    private PacienteExcelImporter excelImporter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaRetornarListaDePacientes() throws Exception {
        Paciente p1 = new Paciente();
        p1.setNombreMascota("Firulais");
        Mockito.when(repository.findAll()).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deberiaRetornarPacientePorIdSiExiste() throws Exception {
        Paciente p = new Paciente();
        p.setId(1L);
        p.setNombreMascota("Firulais");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreMascota").value("Firulais"));
    }

    @Test
    void deberiaRetornar404SiPacienteNoExiste() throws Exception {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaCrearPacienteCorrectamente() throws Exception {
        Paciente nuevo = new Paciente();
        nuevo.setNombreMascota("Toby");
        nuevo.setEspecie("Perro");
        nuevo.setRaza("Beagle");
        nuevo.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        nuevo.setTipoIdentificacion("CC");
        nuevo.setNumeroIdentificacion("123456");
        nuevo.setNombreDueno("Carlos");
        nuevo.setCiudad("Bogotá");
        nuevo.setDireccion("Calle 123");
        nuevo.setTelefono("321654987");

        Mockito.when(repository.save(Mockito.any())).thenAnswer(i -> {
            Paciente guardado = i.getArgument(0);
            guardado.setId(100L);
            return guardado;
        });

        mockMvc.perform(post("/api/pacientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Paciente guardado con ID: 100"));
    }

    @Test
    void deberiaRetornar400SiFaltanCamposObligatorios() throws Exception {
        Paciente invalido = new Paciente(); // Sin nombreMascota, especie, etc.

        mockMvc.perform(post("/api/pacientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
            .andExpect(status().isBadRequest());
    }
}
