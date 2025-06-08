package com.prueba.veterinaria.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PacienteValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // con este test validamos que un campo obligatorio no peude estar vacio
    @Test
    void nombreMascotaNoDebeSerVacio() {
        Paciente p = crearPacienteValido();
        p.setNombreMascota(""); //

        Set<ConstraintViolation<Paciente>> violations = validator.validate(p);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombreMascota")));
    }

    // con este test validamos que un campo obligatorio no peude estar vacio o con espacios
    @Test
    void nombreMascotaNoDebeSerSoloEspacios() {
        Paciente p = crearPacienteValido();
        p.setNombreMascota("   "); // Solo espacios

        Set<ConstraintViolation<Paciente>> violations = validator.validate(p);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombreMascota")));
    }

    // campo que tenemos marcado como not null no peude estar vacio

    @Test
    void fechaNacimientoNoDebeSerNull() {
        Paciente p = crearPacienteValido();
        p.setFechaNacimiento(null);
        Set<ConstraintViolation<Paciente>> violations = validator.validate(p);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaNacimiento")));
    }

    // test para ver si el paciente tiene todos sus campos validos

    @Test
    void pacienteValidoNoDebeTenerViolaciones() {
        Paciente p = crearPacienteValido();
        Set<ConstraintViolation<Paciente>> violations = validator.validate(p);
        assertTrue(violations.isEmpty());
    }

    // ejemplo de paciente valido para hacer los test

    private Paciente crearPacienteValido() {
        Paciente p = new Paciente();
        p.setNombreMascota("Firulais");
        p.setEspecie("Perro");
        p.setRaza("Beagle");
        p.setFechaNacimiento(java.time.LocalDate.now());
        p.setTipoIdentificacion("CC");
        p.setNumeroIdentificacion("123");
        p.setNombreDueno("Juan");
        p.setCiudad("Bogotá");
        p.setDireccion("Calle 1");
        p.setTelefono("123456789");
        return p;
    }
}