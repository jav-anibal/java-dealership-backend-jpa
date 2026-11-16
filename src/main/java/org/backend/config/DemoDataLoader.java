package org.backend.config;

import org.backend.model.Concesionario;
import org.backend.model.Coche;
import org.backend.model.Equipamiento;
import org.backend.model.Mecanico;
import org.backend.repository.CocheRepository;
import org.backend.repository.ConcesionarioRepository;
import org.backend.repository.EquipamientoRepository;
import org.backend.repository.MecanicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/** Datos de ejemplo para probar la API en Swagger. Solo con el perfil "demo". */
@Component
@Profile("demo")
public class DemoDataLoader implements CommandLineRunner {

    private final ConcesionarioRepository concesionarios;
    private final CocheRepository coches;
    private final EquipamientoRepository equipamientos;
    private final MecanicoRepository mecanicos;

    public DemoDataLoader(ConcesionarioRepository concesionarios, CocheRepository coches,
                          EquipamientoRepository equipamientos, MecanicoRepository mecanicos) {
        this.concesionarios = concesionarios;
        this.coches = coches;
        this.equipamientos = equipamientos;
        this.mecanicos = mecanicos;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (concesionarios.count() > 0) {
            return;
        }
        Concesionario c = concesionarios.save(new Concesionario("AutosMiravent", "Avenida del Carnaval"));
        coches.save(new Coche("8910JCY", "Nissan", "Pulsar", BigDecimal.valueOf(20000), c));
        coches.save(new Coche("1234ABC", "Toyota", "Corolla", BigDecimal.valueOf(25000), c));
        coches.save(new Coche("5678DEF", "Ford", "Focus", BigDecimal.valueOf(18000), c));
        equipamientos.save(new Equipamiento("Climatizador", BigDecimal.valueOf(1000)));
        equipamientos.save(new Equipamiento("Techo solar", BigDecimal.valueOf(1500)));
        mecanicos.save(new Mecanico("Juan Perez"));
        mecanicos.save(new Mecanico("Luis Sanchez"));
    }
}
