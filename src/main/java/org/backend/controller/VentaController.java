package org.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.backend.dto.VentaRequest;
import org.backend.dto.VentaResponse;
import org.backend.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Vender un coche",
            description = "Falla con 409 si el coche ya esta vendido y con 404 si no existe.")
    public VentaResponse vender(@Valid @RequestBody VentaRequest req) {
        return service.vender(req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una venta")
    public VentaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}
