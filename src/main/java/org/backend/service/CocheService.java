package org.backend.service;

import org.backend.dto.*;
import org.backend.exception.ConflictException;
import org.backend.exception.ResourceNotFoundException;
import org.backend.model.*;
import org.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CocheService {

    private final CocheRepository coches;
    private final ConcesionarioRepository concesionarios;
    private final EquipamientoRepository equipamientos;
    private final MecanicoRepository mecanicos;
    private final ReparacionRepository reparaciones;

    public CocheService(CocheRepository coches, ConcesionarioRepository concesionarios,
                        EquipamientoRepository equipamientos, MecanicoRepository mecanicos,
                        ReparacionRepository reparaciones) {
        this.coches = coches;
        this.concesionarios = concesionarios;
        this.equipamientos = equipamientos;
        this.mecanicos = mecanicos;
        this.reparaciones = reparaciones;
    }

    @Transactional
    public CocheResponse crear(CocheRequest req) {
        String matricula = normalizar(req.matricula());
        if (coches.existsById(matricula)) {
            throw new ConflictException("Ya existe un coche con matricula: " + matricula);
        }
        Concesionario concesionario = concesionarios.findById(req.concesionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe concesionario con ID: " + req.concesionarioId()));
        Coche coche = new Coche(matricula, req.marca(), req.modelo(), req.precioBase(), concesionario);
        return CocheResponse.from(coches.save(coche));
    }

    public List<CocheResponse> listar(Long concesionarioId) {
        List<Coche> resultado = concesionarioId == null
                ? coches.findAll()
                : coches.findByConcesionarioId(concesionarioId);
        return resultado.stream().map(CocheResponse::from).toList();
    }

    public CocheResponse obtener(String matricula) {
        return CocheResponse.from(buscar(matricula));
    }

    /** Instala un extra en el coche y devuelve el precio total (base + extras). */
    @Transactional
    public PrecioTotalResponse instalarExtra(String matricula, Long idEquipamiento) {
        Coche coche = buscar(matricula);
        Equipamiento equipamiento = equipamientos.findById(idEquipamiento)
                .orElseThrow(() -> new ResourceNotFoundException("No existe equipamiento con ID: " + idEquipamiento));
        coche.getEquipamientos().add(equipamiento);

        BigDecimal extras = coche.getEquipamientos().stream()
                .map(Equipamiento::getPrecioUnit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PrecioTotalResponse(coche.getMatricula(), coche.getPrecioBase(), extras,
                coche.getPrecioBase().add(extras));
    }

    @Transactional
    public ReparacionResponse registrarReparacion(String matricula, ReparacionRequest req) {
        Coche coche = buscar(matricula);
        Mecanico mecanico = mecanicos.findById(req.mecanicoId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe mecanico con ID: " + req.mecanicoId()));
        Reparacion reparacion = new Reparacion(req.fecha(), req.coste(), req.descripcion(), coche, mecanico);
        return ReparacionResponse.from(reparaciones.save(reparacion));
    }

    /** Coste actual = precio de compra + reparaciones + extras. Solo para coches vendidos. */
    public CosteActualResponse costeActual(String matricula) {
        Coche coche = buscar(matricula);
        if (!coche.isVendido()) {
            throw new ConflictException("El coche " + coche.getMatricula() + " no tiene propietario (no esta vendido)");
        }
        BigDecimal precioCompra = coche.getVenta().getPrecioFinal();
        BigDecimal reparacionesTotal = nvl(reparaciones.sumCostePorCoche(coche.getMatricula()));
        BigDecimal extras = nvl(coches.sumExtras(coche.getMatricula()));
        return new CosteActualResponse(coche.getMatricula(), precioCompra, reparacionesTotal, extras,
                precioCompra.add(reparacionesTotal).add(extras));
    }

    private Coche buscar(String matricula) {
        String m = normalizar(matricula);
        return coches.findById(m)
                .orElseThrow(() -> new ResourceNotFoundException("No existe coche con matricula: " + m));
    }

    private static String normalizar(String matricula) {
        return matricula.trim().toUpperCase();
    }

    private static BigDecimal nvl(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
