package com.rysia.conferencedemo.configuration;

import com.rysia.conferencedemo.controllers.HomeController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    private final HomeController homeController;

    public SwaggerConfiguration(HomeController homeController) {
        this.homeController = homeController;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(regex("/api.*"))
                .apis(RequestHandlerSelectors.basePackage("com.rysia.conferencedemo"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(homeController.getName().toUpperCase().concat(" REST API"))
                .description("Conference REST API Documentation")
                .version(homeController.getVersion())
                .license(homeController.getLicense())
                .licenseUrl(homeController.getLicenseURL())
                .contact(new Contact(
                        homeController.getDeveloperName(),
                        homeController.getDeveloper(),
                        homeController.getContact()))
                .build();
    }
}
