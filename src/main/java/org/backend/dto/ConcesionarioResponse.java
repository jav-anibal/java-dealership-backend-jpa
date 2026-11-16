package org.backend.dto;

import org.backend.model.Concesionario;

public record ConcesionarioResponse(Long id, String nombre, String direccion) {

    public static ConcesionarioResponse from(Concesionario c) {
        return new ConcesionarioResponse(c.getId(), c.getNombre(), c.getDireccion());
    }
}
