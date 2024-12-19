package py.una.pol.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Esta clase es la configuración de Swagger API para la gestión de Usuarios, Roles y Permisos
 */
@Configuration
public class SwaggerConfig {
    /**
     * Crea una instancia personalizada de OpenAPI con la información de la API.
     *
     * @return una instancia de OpenAPI con la configuración definida.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Autenticación")
                        .version("1.0")
                        .description("API para la gestión de Usuarios, Roles y Permisos"));

    }
}
