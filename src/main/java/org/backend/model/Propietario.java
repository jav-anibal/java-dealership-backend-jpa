package org.backend.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "propietarios")
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9, nullable = false, unique = true)
    private String dni;

    @Column(length = 100, nullable = false)
    private String nombre;

    protected Propietario() {
    }

    public Propietario(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Propietario that)) return false;
        return Objects.equals(dni, that.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }
}
