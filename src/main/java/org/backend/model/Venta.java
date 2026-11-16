package org.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioFinal;

    // 1:1 - unique: la base de datos impide vender dos veces el mismo coche
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coche_matricula", nullable = false, unique = true)
    private Coche coche;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concesionario_id", nullable = false)
    private Concesionario concesionario;

    protected Venta() {
    }

    public Venta(LocalDate fecha, BigDecimal precioFinal, Coche coche, Propietario propietario, Concesionario concesionario) {
        this.fecha = fecha;
        this.precioFinal = precioFinal;
        this.coche = coche;
        this.propietario = propietario;
        this.concesionario = concesionario;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public Coche getCoche() {
        return coche;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public Concesionario getConcesionario() {
        return concesionario;
    }
}
