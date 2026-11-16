package org.backend.repository;

import org.backend.model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CocheRepository extends JpaRepository<Coche, String> {

    List<Coche> findByConcesionarioId(Long concesionarioId);

    /** Stock: coches del concesionario que aun no tienen venta. */
    @Query("SELECT c FROM Coche c WHERE c.concesionario.id = :idConcesionario AND c.venta IS NULL")
    List<Coche> findStock(@Param("idConcesionario") Long idConcesionario);

    /** Suma de los extras instalados en un coche (null si no tiene). */
    @Query("SELECT SUM(e.precioUnit) FROM Coche c JOIN c.equipamientos e WHERE c.matricula = :matricula")
    BigDecimal sumExtras(@Param("matricula") String matricula);
}
