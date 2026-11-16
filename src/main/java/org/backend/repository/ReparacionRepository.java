package org.backend.repository;

import org.backend.model.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {

    List<Reparacion> findByMecanicoIdOrderByFechaDesc(Long mecanicoId);

    @Query("SELECT SUM(r.coste) FROM Reparacion r WHERE r.coche.matricula = :matricula")
    BigDecimal sumCostePorCoche(@Param("matricula") String matricula);
}
