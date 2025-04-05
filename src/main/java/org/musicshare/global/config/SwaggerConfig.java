package org.musicshare.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI () {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("MusicShare API")
            .description("음악공유 서비스")
            .version("1..0.0");
    }

    private SecurityScheme createAPIKeyScheme(){
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
            .group("auth")
            .pathsToMatch("/auth/**")
            .build();
    }

    @Bean
    public GroupedOpenApi emailApi() {
        return GroupedOpenApi.builder()
            .group("email")
            .pathsToMatch("/email/**")
            .build();
    }

    @Bean
    public GroupedOpenApi likeApi() {
        return GroupedOpenApi.builder()
            .group("like")
            .pathsToMatch("/like/**")
            .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
            .group("member")
            .pathsToMatch("/member/**")
            .build();
    }

    @Bean
    public GroupedOpenApi fanApi() {
        return GroupedOpenApi.builder()
            .group("fan")
            .pathsToMatch("/fan/**")
            .build();
    }

    @Bean
    public GroupedOpenApi musicApi() {
        return GroupedOpenApi.builder()
            .group("music")
            .pathsToMatch("/music/**")
            .build();
    }

    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder()
            .group("comment")
            .pathsToMatch("/comment/**")
            .build();
    }

    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
            .group("common")
            .pathsToMatch("/common/**")
            .build();
    }

}