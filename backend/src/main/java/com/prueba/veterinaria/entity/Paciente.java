package com.prueba.veterinaria.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paciente de la veterinaria")
@Entity
public class Paciente {

    @Schema(description = "ID del paciente", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String nombreMascota;
    @Schema(description = "Especie de la mascota", example = "Perro")
    private String especie;
    @Schema(description = "Raza de la mascota", example = "Labrador")
    private String raza;
    @Schema(description = "Fecha de nacimiento de la mascota", example = "2020-01-01")
    private LocalDate fechaNacimiento;

    @Schema(description = "Tipo de identificación del dueño", example = "DNI")
    private String tipoIdentificacion;
    @Schema(description = "Número de identificación del dueño", example = "12345678")
    private String numeroIdentificacion;
    @Schema(description = "Nombre del dueño", example = "Juan Perez")
    private String nombreDueno;

    @Schema(description = "Ciudad de residencia", example = "Ciudad")
    private String ciudad;
    @Schema(description = "Dirección del dueño", example = "Calle 123")
    private String direccion;
    @Schema(description = "Teléfono de contacto", example = "123456789")
    private String telefono;

    @Schema(description = "Fecha de registro del paciente", example = "2024-06-06")
    private LocalDate fechaRegistro;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
