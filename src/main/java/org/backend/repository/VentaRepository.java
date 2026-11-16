package org.backend.repository;

import org.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByConcesionarioId(Long concesionarioId);

    @Query("SELECT SUM(v.precioFinal) FROM Venta v WHERE v.concesionario.id = :idConcesionario")
    BigDecimal totalRecaudado(@Param("idConcesionario") Long idConcesionario);
}
