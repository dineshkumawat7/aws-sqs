package com.amazon.aws.sqs.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Product API")
                        .description("AWS sqs api")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Dinesh Kumawat")
                                .email("dkumawat7627@gmail.com")
                                .url("https://github.com/dineshkumawat7"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("AWS SQS API Documentation")
                        .url("https://product-api.wiki.github.org/docs"));
    }
}
