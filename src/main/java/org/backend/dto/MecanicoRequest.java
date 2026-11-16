package org.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MecanicoRequest(@NotBlank @Size(max = 100) String nombre) {
}
