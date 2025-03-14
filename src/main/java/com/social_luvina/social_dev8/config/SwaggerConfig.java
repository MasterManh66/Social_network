package com.social_luvina.social_dev8.config;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  
  @Bean
  public OpenAPI customOpenApi(){
    return new OpenAPI()
              .info(new Info().title("ManhTran").version("version").description("description")
              .license(new License().name("Api license").url("http://domain.vn/license")))
              .servers(List.of(new Server().url("http://localhost:8080").description("serverName")))
              .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")))
              .security(List.of(new SecurityRequirement().addList("bearerAuth")));
  }

  @Bean
  public GroupedOpenApi groupedOpenApi() {
      return GroupedOpenApi.builder()
              .group("api-service-v1")
              .packagesToScan("com.social_luvina.social_dev8.modules.controllers")
              .build();
  }

}
