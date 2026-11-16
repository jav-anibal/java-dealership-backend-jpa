package org.backend.dto;

import java.math.BigDecimal;

/** costeTotal = precio de compra + reparaciones + extras. */
public record CosteActualResponse(String matricula, BigDecimal precioCompra, BigDecimal totalReparaciones,
                                  BigDecimal totalExtras, BigDecimal costeTotal) {
}
