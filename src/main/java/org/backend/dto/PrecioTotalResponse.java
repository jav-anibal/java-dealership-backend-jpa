package org.backend.dto;

import java.math.BigDecimal;

/** precioTotal = precioBase + suma de extras instalados. */
public record PrecioTotalResponse(String matricula, BigDecimal precioBase, BigDecimal totalExtras,
                                  BigDecimal precioTotal) {
}
