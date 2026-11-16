package org.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "coches")
public class Coche {

    @Id
    @Column(length = 7, updatable = false)
    private String matricula;

    @Column(length = 32, nullable = false)
    private String marca;

    @Column(length = 32, nullable = false)
    private String modelo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioBase;

    // N:1 - un coche pertenece a un concesionario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concesionario_id", nullable = false)
    private Concesionario concesionario;

    // N:M - tabla intermedia coche_equipamiento
    @ManyToMany
    @JoinTable(
            name = "coche_equipamiento",
            joinColumns = @JoinColumn(name = "coche_matricula"),
            inverseJoinColumns = @JoinColumn(name = "equipamiento_id")
    )
    private Set<Equipamiento> equipamientos = new HashSet<>();

    // 1:N - lado inverso; la FK vive en Reparacion
    @OneToMany(mappedBy = "coche")
    private List<Reparacion> reparaciones = new ArrayList<>();

    // 1:1 - lado inverso; la FK (unique) vive en Venta
    @OneToOne(mappedBy = "coche")
    private Venta venta;

    protected Coche() {
    }

    public Coche(String matricula, String marca, String modelo, BigDecimal precioBase, Concesionario concesionario) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.precioBase = precioBase;
        this.concesionario = concesionario;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public Concesionario getConcesionario() {
        return concesionario;
    }

    public Set<Equipamiento> getEquipamientos() {
        return equipamientos;
    }

    public List<Reparacion> getReparaciones() {
        return reparaciones;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public boolean isVendido() {
        return venta != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coche coche)) return false;
        return Objects.equals(matricula, coche.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matricula);
    }
}
