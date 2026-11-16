package org.backend.repository;

import org.backend.model.Concesionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {
}
