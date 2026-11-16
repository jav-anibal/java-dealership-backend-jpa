package org.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "equipamientos")
public class Equipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnit;

    protected Equipamiento() {
    }

    public Equipamiento(String nombre, BigDecimal precioUnit) {
        this.nombre = nombre;
        this.precioUnit = precioUnit;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecioUnit() {
        return precioUnit;
    }
}
