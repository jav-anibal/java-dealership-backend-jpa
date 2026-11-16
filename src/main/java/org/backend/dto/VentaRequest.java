package org.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** El concesionario de la venta es el del coche; no se envia en la peticion. */
public record VentaRequest(
        @NotBlank @Pattern(regexp = "(?i)^\\d{4}[A-Z]{3}$", message = "Formato: 4 digitos + 3 letras (ej: 1234ABC)")
        String matricula,
        @Schema(example = "12345678A")
        @NotBlank @Pattern(regexp = "(?i)^\\d{8}[A-Z]$", message = "Formato: 8 digitos + 1 letra (ej: 12345678A)")
        String dniComprador,
        @NotBlank @Size(max = 100) String nombreComprador,
        @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal precioFinal) {
}
