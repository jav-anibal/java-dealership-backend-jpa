package org.backend.dto;

import org.backend.model.Reparacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReparacionResponse(Long id, String matricula, Long mecanicoId, LocalDate fecha,
                                 BigDecimal coste, String descripcion) {

    public static ReparacionResponse from(Reparacion r) {
        return new ReparacionResponse(r.getId(), r.getCoche().getMatricula(), r.getMecanico().getId(),
                r.getFecha(), r.getCoste(), r.getDescripcion());
    }
}
