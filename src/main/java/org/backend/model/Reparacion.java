package org.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reparaciones")
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal coste;

    @Column(length = 255, nullable = false)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coche_matricula", nullable = false)
    private Coche coche;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mecanico_id", nullable = false)
    private Mecanico mecanico;

    protected Reparacion() {
    }

    public Reparacion(LocalDate fecha, BigDecimal coste, String descripcion, Coche coche, Mecanico mecanico) {
        this.fecha = fecha;
        this.coste = coste;
        this.descripcion = descripcion;
        this.coche = coche;
        this.mecanico = mecanico;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Coche getCoche() {
        return coche;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }
}
