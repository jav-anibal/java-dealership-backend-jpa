package org.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.backend.dto.*;
import org.backend.service.CatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Taller", description = "Equipamientos y mecanicos")
public class TallerController {

    private final CatalogoService service;

    public TallerController(CatalogoService service) {
        this.service = service;
    }

    @PostMapping("/equipamientos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Dar de alta un equipamiento")
    public EquipamientoResponse crearEquipamiento(@Valid @RequestBody EquipamientoRequest req) {
        return service.crearEquipamiento(req);
    }

    @GetMapping("/equipamientos")
    @Operation(summary = "Listar equipamientos")
    public List<EquipamientoResponse> listarEquipamientos() {
        return service.listarEquipamientos();
    }

    @PostMapping("/mecanicos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Dar de alta un mecanico")
    public MecanicoResponse crearMecanico(@Valid @RequestBody MecanicoRequest req) {
        return service.crearMecanico(req);
    }

    @GetMapping("/mecanicos")
    @Operation(summary = "Listar mecanicos")
    public List<MecanicoResponse> listarMecanicos() {
        return service.listarMecanicos();
    }

    @GetMapping("/mecanicos/{id}/reparaciones")
    @Operation(summary = "Historial de reparaciones de un mecanico (mas recientes primero)")
    public List<ReparacionResponse> historial(@PathVariable Long id) {
        return service.historialDeMecanico(id);
    }
}
