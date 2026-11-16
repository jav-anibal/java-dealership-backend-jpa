package org.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.backend.dto.*;
import org.backend.service.ConcesionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concesionarios")
@Tag(name = "Concesionarios", description = "Altas, stock, ventas e informes por concesionario")
public class ConcesionarioController {

    private final ConcesionarioService service;

    public ConcesionarioController(ConcesionarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Dar de alta un concesionario")
    public ConcesionarioResponse crear(@Valid @RequestBody ConcesionarioRequest req) {
        return service.crear(req);
    }

    @GetMapping
    @Operation(summary = "Listar concesionarios")
    public List<ConcesionarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un concesionario")
    public ConcesionarioResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @GetMapping("/{id}/stock")
    @Operation(summary = "Stock: coches del concesionario sin vender")
    public List<CocheResponse> stock(@PathVariable Long id) {
        return service.stock(id);
    }

    @GetMapping("/{id}/ventas")
    @Operation(summary = "Ventas realizadas por el concesionario")
    public List<VentaResponse> ventas(@PathVariable Long id) {
        return service.ventas(id);
    }

    @GetMapping("/{id}/recaudacion")
    @Operation(summary = "Total recaudado (suma de precios finales de venta)")
    public RecaudacionResponse recaudacion(@PathVariable Long id) {
        return service.recaudacion(id);
    }
}
