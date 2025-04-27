package com.app;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupedOpenApiConfig {

    @Bean
    public GroupedOpenApi boApiGroup(@Qualifier("globalApiCustomizer") OpenApiCustomizer globalCustomOpenApiCustomizer) {
        return GroupedOpenApi.builder()
            .group("bo-api")
            .pathsToMatch("/api/v1/**")
            .addOpenApiCustomizer(globalCustomOpenApiCustomizer)
            .build();
    }

    @Bean
    public GroupedOpenApi commonApiGroup(@Qualifier("globalApiCustomizer")OpenApiCustomizer globalCustomOpenApiCustomizer) {
        return GroupedOpenApi.builder()
            .group("common-api")
            .pathsToMatch("/api/v1/**")   // 실제 경로에 맞게 수정
            .addOpenApiCustomizer(globalCustomOpenApiCustomizer)
            .build();
    }

    @Bean
    public GroupedOpenApi foApiGroup(@Qualifier("globalApiCustomizer")OpenApiCustomizer globalCustomOpenApiCustomizer) {
        return GroupedOpenApi.builder()
            .group("fo-api")
            .pathsToMatch("/api/v1/**")
            .addOpenApiCustomizer(globalCustomOpenApiCustomizer)
            .build();
    }
}
