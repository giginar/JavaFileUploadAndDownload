package com.kucukcinar.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Spring fox config.
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    /**
     * Product api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.kucukcinar.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    /**
     * Swagger: http://localhost:8080/v2/api-docs
     */
}
