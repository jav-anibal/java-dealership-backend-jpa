package org.backend.dto;

import java.math.BigDecimal;

public record RecaudacionResponse(Long concesionarioId, BigDecimal total) {
}
