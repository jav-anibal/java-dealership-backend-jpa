package org.backend.service;

import org.backend.dto.VentaRequest;
import org.backend.dto.VentaResponse;
import org.backend.exception.ConflictException;
import org.backend.exception.ResourceNotFoundException;
import org.backend.model.Coche;
import org.backend.model.Propietario;
import org.backend.model.Venta;
import org.backend.repository.CocheRepository;
import org.backend.repository.PropietarioRepository;
import org.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class VentaService {

    private final VentaRepository ventas;
    private final CocheRepository coches;
    private final PropietarioRepository propietarios;

    public VentaService(VentaRepository ventas, CocheRepository coches, PropietarioRepository propietarios) {
        this.ventas = ventas;
        this.coches = coches;
        this.propietarios = propietarios;
    }

    /**
     * Vende un coche. Una sola transaccion: si algo falla no se crea ni propietario ni venta.
     * El concesionario de la venta es el del coche. Un comprador existente (mismo DNI) se reutiliza.
     */
    @Transactional
    public VentaResponse vender(VentaRequest req) {
        String matricula = req.matricula().trim().toUpperCase();
        String dni = req.dniComprador().trim().toUpperCase();

        Coche coche = coches.findById(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("No existe coche con matricula: " + matricula));
        if (coche.isVendido()) {
            throw new ConflictException("El coche con matricula " + matricula + " ya esta vendido");
        }

        Propietario propietario = propietarios.findByDni(dni)
                .orElseGet(() -> propietarios.save(new Propietario(dni, req.nombreComprador())));

        Venta venta = ventas.save(new Venta(LocalDate.now(), req.precioFinal(), coche, propietario,
                coche.getConcesionario()));
        coche.setVenta(venta);
        return VentaResponse.from(venta);
    }

    public VentaResponse obtener(Long id) {
        return ventas.findById(id)
                .map(VentaResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No existe venta con ID: " + id));
    }
}
