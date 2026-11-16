package org.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DealershipApiIntegrationTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    // ---------- helpers ----------

    /** Compara importes ignorando la escala (26000 == 26000.00). */
    private ResultMatcher decimal(String jsonPointer, String esperado) {
        return result -> {
            JsonNode nodo = mapper.readTree(result.getResponse().getContentAsString()).at(jsonPointer);
            org.junit.jupiter.api.Assertions.assertEquals(0,
                    new BigDecimal(esperado).compareTo(new BigDecimal(nodo.asText())),
                    "Importe distinto en " + jsonPointer + ": " + nodo);
        };
    }

    private ResultActions postJson(String url, String json) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private long crearConcesionario() throws Exception {
        String body = postJson("/api/concesionarios", """
                {"nombre":"AutosMiravent","direccion":"Avenida del Carnaval"}""")
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(body).get("id").asLong();
    }

    private void crearCoche(String matricula, long concesionarioId, String precio) throws Exception {
        postJson("/api/coches", """
                {"matricula":"%s","marca":"Toyota","modelo":"Corolla","precioBase":%s,"concesionarioId":%d}"""
                .formatted(matricula, precio, concesionarioId))
                .andExpect(status().isCreated());
    }

    private long crearId(String url, String json) throws Exception {
        String body = postJson(url, json).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = mapper.readTree(body);
        return node.get("id").asLong();
    }

    private ResultActions vender(String matricula, String dni, String precio) throws Exception {
        return postJson("/api/ventas", """
                {"matricula":"%s","dniComprador":"%s","nombreComprador":"Maria Garcia","precioFinal":%s}"""
                .formatted(matricula, dni, precio));
    }

    // ---------- tests ----------

    @Test
    void flujoCompleto_stock_extras_reparacion_venta_y_coste() throws Exception {
        long concesionario = crearConcesionario();
        crearCoche("1234ABC", concesionario, "25000");
        long equipamiento = crearId("/api/equipamientos", """
                {"nombre":"Climatizador","precioUnit":1000}""");
        long mecanico = crearId("/api/mecanicos", """
                {"nombre":"Juan Perez"}""");

        mvc.perform(get("/api/concesionarios/{id}/stock", concesionario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mvc.perform(post("/api/coches/{m}/equipamientos/{e}", "1234ABC", equipamiento))
                .andExpect(status().isOk())
                .andExpect(decimal("/precioTotal", "26000"));

        postJson("/api/coches/1234ABC/reparaciones", """
                {"mecanicoId":%d,"fecha":"2026-11-01","coste":350,"descripcion":"Cambio de aceite"}"""
                .formatted(mecanico))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricula").value("1234ABC"));

        vender("1234ABC", "12345678A", "26000")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.concesionarioId").value(concesionario))
                .andExpect(jsonPath("$.matricula").value("1234ABC"));

        // vendido => ya no esta en stock
        mvc.perform(get("/api/concesionarios/{id}/stock", concesionario))
                .andExpect(jsonPath("$", hasSize(0)));

        mvc.perform(get("/api/concesionarios/{id}/recaudacion", concesionario))
                .andExpect(decimal("/total", "26000"));

        // compra 26000 + reparaciones 350 + extras 1000
        mvc.perform(get("/api/coches/1234ABC/coste-actual"))
                .andExpect(status().isOk())
                .andExpect(decimal("/costeTotal", "27350"));

        mvc.perform(get("/api/mecanicos/{id}/reparaciones", mecanico))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void vender_un_coche_ya_vendido_devuelve_409() throws Exception {
        long concesionario = crearConcesionario();
        crearCoche("1234ABC", concesionario, "25000");

        vender("1234ABC", "12345678A", "24000").andExpect(status().isCreated());
        vender("1234ABC", "87654321B", "24000")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(containsString("ya esta vendido")));
    }

    @Test
    void mismo_comprador_se_reutiliza_por_dni() throws Exception {
        long concesionario = crearConcesionario();
        crearCoche("1111AAA", concesionario, "10000");
        crearCoche("2222BBB", concesionario, "12000");

        String uno = vender("1111AAA", "12345678A", "10000").andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String dos = vender("2222BBB", "12345678a", "12000").andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        org.junit.jupiter.api.Assertions.assertEquals(
                mapper.readTree(uno).get("propietarioId").asLong(),
                mapper.readTree(dos).get("propietarioId").asLong());
    }

    @Test
    void matricula_invalida_devuelve_400_con_detalle_por_campo() throws Exception {
        long concesionario = crearConcesionario();
        postJson("/api/coches", """
                {"matricula":"XX","marca":"Toyota","modelo":"Corolla","precioBase":100,"concesionarioId":%d}"""
                .formatted(concesionario))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.matricula").exists());
    }

    @Test
    void coche_inexistente_devuelve_404() throws Exception {
        mvc.perform(get("/api/coches/9999ZZZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    void coche_duplicado_devuelve_409() throws Exception {
        long concesionario = crearConcesionario();
        crearCoche("1234ABC", concesionario, "25000");
        postJson("/api/coches", """
                {"matricula":"1234abc","marca":"Ford","modelo":"Focus","precioBase":100,"concesionarioId":%d}"""
                .formatted(concesionario))
                .andExpect(status().isConflict());
    }

    @Test
    void coste_actual_de_coche_sin_vender_devuelve_409() throws Exception {
        long concesionario = crearConcesionario();
        crearCoche("1234ABC", concesionario, "25000");
        mvc.perform(get("/api/coches/1234ABC/coste-actual"))
                .andExpect(status().isConflict());
    }

    @Test
    void swagger_y_openapi_estan_publicados() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/api/ventas")))
                .andExpect(content().string(containsString("Dealership ERP API")));
    }
}
