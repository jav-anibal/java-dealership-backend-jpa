package org.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReparacionRequest(
        @NotNull Long mecanicoId,
        @NotNull LocalDate fecha,
        @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal coste,
        @NotBlank @Size(max = 255) String descripcion) {
}
