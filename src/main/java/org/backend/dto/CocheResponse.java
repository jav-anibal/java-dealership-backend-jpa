package org.backend.dto;

import org.backend.model.Coche;

import java.math.BigDecimal;

public record CocheResponse(String matricula, String marca, String modelo, BigDecimal precioBase,
                            Long concesionarioId, boolean vendido) {

    public static CocheResponse from(Coche c) {
        return new CocheResponse(c.getMatricula(), c.getMarca(), c.getModelo(), c.getPrecioBase(),
                c.getConcesionario().getId(), c.isVendido());
    }
}
