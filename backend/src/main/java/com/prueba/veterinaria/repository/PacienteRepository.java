package com.prueba.veterinaria.repository;

import com.prueba.veterinaria.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // ¡Spring implementa todos los métodos CRUD por ti!
}
