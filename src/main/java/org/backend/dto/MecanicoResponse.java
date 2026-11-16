package org.backend.dto;

import org.backend.model.Mecanico;

public record MecanicoResponse(Long id, String nombre) {

    public static MecanicoResponse from(Mecanico m) {
        return new MecanicoResponse(m.getId(), m.getNombre());
    }
}
