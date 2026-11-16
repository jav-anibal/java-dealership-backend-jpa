package org.backend.dto;

import org.backend.model.Equipamiento;

import java.math.BigDecimal;

public record EquipamientoResponse(Long id, String nombre, BigDecimal precioUnit) {

    public static EquipamientoResponse from(Equipamiento e) {
        return new EquipamientoResponse(e.getId(), e.getNombre(), e.getPrecioUnit());
    }
}
