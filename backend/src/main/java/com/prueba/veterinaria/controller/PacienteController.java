package com.prueba.veterinaria.controller;

import com.prueba.veterinaria.entity.Paciente;
import com.prueba.veterinaria.repository.PacienteRepository;
import com.prueba.veterinaria.service.PacienteExcelExporter;
import com.prueba.veterinaria.service.PacienteExcelImporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*") // permite peticiones desde el frontend
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private PacienteExcelExporter excelExporter;

    @Autowired
    private PacienteExcelImporter excelImporter;

    @Operation(summary = "Obtiene todos los pacientes", description = "Devuelve una lista de todos los pacientes registrados")
    @GetMapping
    public List<Paciente> obtenerTodos() {
        return repository.findAll();
    }

    @Operation(summary = "Obtiene un paciente por ID")
    @GetMapping("/{id}")
    public Paciente obtenerPorId(
        @Parameter(description = "ID del paciente", example = "1")
        @PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @Operation(summary = "Crea un nuevo paciente")
    @PostMapping
    public Paciente crear(@RequestBody Paciente paciente) {
        paciente.setFechaRegistro(LocalDate.now());
        return repository.save(paciente);
    }

    @Operation(summary = "Actualiza un paciente por ID")
    @PutMapping("/{id}")
    public Paciente actualizar(@Parameter(description = "ID del paciente", example = "1") @PathVariable Long id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        return repository.save(paciente);
    }

    @Operation(summary = "Elimina un paciente por ID")
    @DeleteMapping("/{id}")
    public void eliminar(@Parameter(description = "ID del paciente", example = "1") @PathVariable Long id) {
        repository.deleteById(id);
    }

    @Operation(summary = "Exporta todos los pacientes a un archivo Excel")
    @GetMapping("/export")
    public void exportarExcel(HttpServletResponse response) throws java.io.IOException {
        List<Paciente> lista = repository.findAll();
        excelExporter.export(lista, response);
    }

    @Operation(summary = "Importa pacientes desde un archivo Excel (.xlsx)")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Paciente> importarExcel(
        @Parameter(description = "Archivo Excel (.xlsx)", required = true)
        @RequestPart("file") MultipartFile file
    ) throws java.io.IOException {
        List<Paciente> pacientes = excelImporter.importar(file);
        return repository.saveAll(pacientes);
    }
}
