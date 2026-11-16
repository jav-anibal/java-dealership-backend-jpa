package org.backend.dto;

import org.backend.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VentaResponse(Long id, LocalDate fecha, BigDecimal precioFinal, String matricula,
                            Long propietarioId, String dniComprador, Long concesionarioId) {

    public static VentaResponse from(Venta v) {
        return new VentaResponse(v.getId(), v.getFecha(), v.getPrecioFinal(), v.getCoche().getMatricula(),
                v.getPropietario().getId(), v.getPropietario().getDni(), v.getConcesionario().getId());
    }
}
