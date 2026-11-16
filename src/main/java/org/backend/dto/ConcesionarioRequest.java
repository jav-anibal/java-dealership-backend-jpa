package org.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConcesionarioRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 150) String direccion) {
}
