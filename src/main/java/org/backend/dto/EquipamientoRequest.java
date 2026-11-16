package org.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EquipamientoRequest(
        @NotBlank @Size(max = 40) String nombre,
        @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal precioUnit) {
}
