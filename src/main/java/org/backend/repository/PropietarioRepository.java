package org.backend.repository;

import org.backend.model.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Optional<Propietario> findByDni(String dni);
}
