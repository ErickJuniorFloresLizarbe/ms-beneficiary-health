package pe.edu.vallegrande.health.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .servers(Collections
                        .singletonList(new Server().url("https://congenial-xylophone-69x56qqv5x624rg7-8086.app.github.dev/")))
                .info(new Info()

                        .title("TRADUCTOR")
                        .description("AS222S5_T03_be")
                        .version("1.0.0")

                );
    }
}