package org.backend.service;

import org.backend.dto.*;
import org.backend.exception.ResourceNotFoundException;
import org.backend.model.Concesionario;
import org.backend.repository.CocheRepository;
import org.backend.repository.ConcesionarioRepository;
import org.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConcesionarioService {

    private final ConcesionarioRepository concesionarios;
    private final CocheRepository coches;
    private final VentaRepository ventas;

    public ConcesionarioService(ConcesionarioRepository concesionarios, CocheRepository coches,
                                VentaRepository ventas) {
        this.concesionarios = concesionarios;
        this.coches = coches;
        this.ventas = ventas;
    }

    @Transactional
    public ConcesionarioResponse crear(ConcesionarioRequest req) {
        return ConcesionarioResponse.from(concesionarios.save(new Concesionario(req.nombre(), req.direccion())));
    }

    public List<ConcesionarioResponse> listar() {
        return concesionarios.findAll().stream().map(ConcesionarioResponse::from).toList();
    }

    public ConcesionarioResponse obtener(Long id) {
        return ConcesionarioResponse.from(buscar(id));
    }

    /** Coches del concesionario que no estan vendidos. */
    public List<CocheResponse> stock(Long id) {
        buscar(id);
        return coches.findStock(id).stream().map(CocheResponse::from).toList();
    }

    public List<VentaResponse> ventas(Long id) {
        buscar(id);
        return ventas.findByConcesionarioId(id).stream().map(VentaResponse::from).toList();
    }

    public RecaudacionResponse recaudacion(Long id) {
        buscar(id);
        BigDecimal total = ventas.totalRecaudado(id);
        return new RecaudacionResponse(id, total != null ? total : BigDecimal.ZERO);
    }

    private Concesionario buscar(Long id) {
        return concesionarios.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe concesionario con ID: " + id));
    }
}
