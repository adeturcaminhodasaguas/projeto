package br.com.caminhodasaguas.api.configs.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Caminho das aguas")
                        .description("REST API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Rodrigo de Oliveira")
                                .email("rodrigooliveirafroesdev@gmail.com"))
                );
    }
}