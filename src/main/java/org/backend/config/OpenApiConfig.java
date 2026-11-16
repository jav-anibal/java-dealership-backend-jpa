package org.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dealershipOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Dealership ERP API")
                .version("1.0.0")
                .description("REST API del ERP de concesionarios: stock, taller, equipamiento, ventas e informes."));
    }
}
