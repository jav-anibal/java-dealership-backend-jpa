package org.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CocheRequest(
        @Schema(example = "1234ABC", description = "4 digitos + 3 letras")
        @NotBlank @Pattern(regexp = "(?i)^\\d{4}[A-Z]{3}$", message = "Formato: 4 digitos + 3 letras (ej: 1234ABC)")
        String matricula,
        @NotBlank @Size(max = 32) String marca,
        @NotBlank @Size(max = 32) String modelo,
        @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal precioBase,
        @NotNull Long concesionarioId) {
}
