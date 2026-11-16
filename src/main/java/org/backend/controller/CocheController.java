package org.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.backend.dto.*;
import org.backend.service.CocheService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coches")
@Tag(name = "Coches", description = "Alta de vehiculos, extras, reparaciones y coste")
public class CocheController {

    private final CocheService service;

    public CocheController(CocheService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Dar de alta un coche en un concesionario")
    public CocheResponse crear(@Valid @RequestBody CocheRequest req) {
        return service.crear(req);
    }

    @GetMapping
    @Operation(summary = "Listar coches (opcionalmente filtrados por concesionario)")
    public List<CocheResponse> listar(
            @Parameter(description = "ID del concesionario") @RequestParam(required = false) Long concesionarioId) {
        return service.listar(concesionarioId);
    }

    @GetMapping("/{matricula}")
    @Operation(summary = "Obtener un coche por matricula")
    public CocheResponse obtener(@PathVariable String matricula) {
        return service.obtener(matricula);
    }

    @PostMapping("/{matricula}/equipamientos/{equipamientoId}")
    @Operation(summary = "Instalar un extra y obtener el precio total (base + extras)")
    public PrecioTotalResponse instalarExtra(@PathVariable String matricula, @PathVariable Long equipamientoId) {
        return service.instalarExtra(matricula, equipamientoId);
    }

    @PostMapping("/{matricula}/reparaciones")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una reparacion del coche")
    public ReparacionResponse registrarReparacion(@PathVariable String matricula,
                                                  @Valid @RequestBody ReparacionRequest req) {
        return service.registrarReparacion(matricula, req);
    }

    @GetMapping("/{matricula}/coste-actual")
    @Operation(summary = "Coste actual: compra + reparaciones + extras (solo coches vendidos)")
    public CosteActualResponse costeActual(@PathVariable String matricula) {
        return service.costeActual(matricula);
    }
}
