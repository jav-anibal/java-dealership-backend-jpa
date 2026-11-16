package org.backend.service;

import org.backend.dto.*;
import org.backend.exception.ResourceNotFoundException;
import org.backend.model.Equipamiento;
import org.backend.model.Mecanico;
import org.backend.repository.EquipamientoRepository;
import org.backend.repository.MecanicoRepository;
import org.backend.repository.ReparacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Equipamientos y mecanicos (datos maestros del taller). */
@Service
@Transactional(readOnly = true)
public class CatalogoService {

    private final EquipamientoRepository equipamientos;
    private final MecanicoRepository mecanicos;
    private final ReparacionRepository reparaciones;

    public CatalogoService(EquipamientoRepository equipamientos, MecanicoRepository mecanicos,
                           ReparacionRepository reparaciones) {
        this.equipamientos = equipamientos;
        this.mecanicos = mecanicos;
        this.reparaciones = reparaciones;
    }

    @Transactional
    public EquipamientoResponse crearEquipamiento(EquipamientoRequest req) {
        return EquipamientoResponse.from(equipamientos.save(new Equipamiento(req.nombre(), req.precioUnit())));
    }

    public List<EquipamientoResponse> listarEquipamientos() {
        return equipamientos.findAll().stream().map(EquipamientoResponse::from).toList();
    }

    @Transactional
    public MecanicoResponse crearMecanico(MecanicoRequest req) {
        return MecanicoResponse.from(mecanicos.save(new Mecanico(req.nombre())));
    }

    public List<MecanicoResponse> listarMecanicos() {
        return mecanicos.findAll().stream().map(MecanicoResponse::from).toList();
    }

    /** Historial de reparaciones de un mecanico, de la mas reciente a la mas antigua. */
    public List<ReparacionResponse> historialDeMecanico(Long id) {
        if (!mecanicos.existsById(id)) {
            throw new ResourceNotFoundException("No existe mecanico con ID: " + id);
        }
        return reparaciones.findByMecanicoIdOrderByFechaDesc(id).stream().map(ReparacionResponse::from).toList();
    }
}
