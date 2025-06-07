package com.prueba.veterinaria.controller;

import com.prueba.veterinaria.entity.Paciente;
import com.prueba.veterinaria.repository.PacienteRepository;
import com.prueba.veterinaria.service.PacienteExcelExporter;
import com.prueba.veterinaria.service.PacienteExcelImporter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private PacienteExcelExporter excelExporter;

    @Autowired
    private PacienteExcelImporter excelImporter;

    @Operation(summary = "Obtiene todos los pacientes")
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Paciente> lista = repository.findAll();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener pacientes.");
        }
    }

    @Operation(summary = "Obtiene un paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Paciente paciente = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Paciente con ID " + id + " no existe."));
            return ResponseEntity.ok(paciente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el paciente.");
        }
    }

    @Operation(summary = "Crea un nuevo paciente")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            String mensaje = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body("Error de validación: " + mensaje);
        }
        try {
            paciente.setFechaRegistro(LocalDate.now());
            Paciente guardado = repository.save(paciente);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Paciente guardado con ID: " + guardado.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear paciente, revisa que los campos sean válidos.");
        }
    }

    @Operation(summary = "Actualiza un paciente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            String mensaje = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body("Error de validación: " + mensaje);
        }
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Paciente con ID " + id + " no existe.");
            }
            paciente.setId(id);
            repository.save(paciente);
            return ResponseEntity.ok("Paciente actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar paciente.");
        }
    }

    @Operation(summary = "Elimina un paciente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se puede eliminar el paciente con ID " + id + ", no existe.");
            }
            repository.deleteById(id);
            return ResponseEntity.ok("Paciente eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar paciente.");
        }
    }

    @Operation(summary = "Exporta todos los pacientes a un archivo Excel")
    @GetMapping("/export")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        List<Paciente> lista = repository.findAll();
        excelExporter.export(lista, response);
    }

    @Operation(summary = "Importa pacientes desde un archivo Excel (.xlsx)")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarExcel(
            @Parameter(description = "Archivo Excel (.xlsx)", required = true)
            @RequestPart("file") MultipartFile file) {
        try {
            List<Paciente> pacientes = excelImporter.importar(file);
            repository.saveAll(pacientes);
            return ResponseEntity.ok("Se importaron " + pacientes.size() + " pacientes correctamente.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Archivo inválido o dañado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar pacientes.");
        }
    }
}
