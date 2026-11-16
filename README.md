# Dealership ERP API

Java backend for a car dealership ERP: JPA/Hibernate domain model (dealerships, cars, equipment, repairs, sales), transactional services and JPQL reports, exposed as a REST API documented with Swagger/OpenAPI.

## Stack

- Java 21 · Spring Boot 3.5 (Web, Data JPA, Validation, Actuator)
- Hibernate 6 / Jakarta Persistence
- PostgreSQL 16 · Flyway (versioned migrations)
- springdoc-openapi (Swagger UI)
- JUnit 5 + MockMvc + H2 (PostgreSQL mode)

## Arquitectura

```text
Controller (REST + validación)  ->  Service (@Transactional)  ->  Repository (Spring Data JPA)  ->  PostgreSQL
            DTOs (records)                reglas de negocio              JPQL                       Flyway
```

## Modelo

- 1:N concesionario → coches
- N:M coches ↔ equipamientos
- 1:N coche → reparaciones (cada una hecha por un mecánico)
- 1:1 coche ↔ venta (`UNIQUE` en BD: un coche no se vende dos veces)
- 1:N propietario → ventas (el comprador se reutiliza por DNI)

## Ejecutar

```bash
cp .env.example .env            # y cambia DB_PASSWORD
docker compose up -d            # PostgreSQL
export DB_PASSWORD=...          # la misma clave del .env
mvn spring-boot:run -Dspring-boot.run.profiles=demo   # "demo" carga datos de ejemplo
```

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>
- Health: <http://localhost:8080/actuator/health>

Tests (no necesitan PostgreSQL, usan H2):

```bash
mvn test
```

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST / GET | `/api/concesionarios` | Alta / listado |
| GET | `/api/concesionarios/{id}` | Detalle |
| GET | `/api/concesionarios/{id}/stock` | Coches sin vender |
| GET | `/api/concesionarios/{id}/ventas` | Ventas del concesionario |
| GET | `/api/concesionarios/{id}/recaudacion` | Total recaudado |
| POST / GET | `/api/coches` | Alta / listado (`?concesionarioId=`) |
| GET | `/api/coches/{matricula}` | Detalle |
| POST | `/api/coches/{matricula}/equipamientos/{id}` | Instalar extra → precio total |
| POST | `/api/coches/{matricula}/reparaciones` | Registrar reparación |
| GET | `/api/coches/{matricula}/coste-actual` | Compra + reparaciones + extras (solo vendidos) |
| POST / GET | `/api/equipamientos` | Alta / listado |
| POST / GET | `/api/mecanicos` | Alta / listado |
| GET | `/api/mecanicos/{id}/reparaciones` | Historial (más recientes primero) |
| POST | `/api/ventas` | Vender un coche |
| GET | `/api/ventas/{id}` | Detalle de venta |

Errores en formato RFC 7807 (`application/problem+json`): `400` validación (con detalle por campo), `404` recurso inexistente, `409` conflicto (coche ya vendido, matrícula duplicada).

## Decisiones

- Esquema gestionado por Flyway; Hibernate solo valida (`ddl-auto=validate`).
- Credenciales por variables de entorno, sin valores por defecto en el repositorio.
- Sin entidades en la API: DTOs como `record` con Bean Validation.
- `open-in-view` desactivado; relaciones `LAZY` mapeadas a DTO dentro de la transacción.
- Origen: proyecto de consola de 2º DAM (JPA/Hibernate puro); el historial de git conserva esa versión.

---

**Anibal Solano** · Backend Developer · Java · Spring Boot · PostgreSQL
