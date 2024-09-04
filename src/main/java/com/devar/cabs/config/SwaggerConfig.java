package com.devar.cabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/swagger-ui/**", "/v2/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }

    @Bean
    public Docket productApi() {
        final ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("X-Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("")
                .required(false)
                .build();
        final List<springfox.documentation.service.Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.devar"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiInfo())
                .globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            "API Title",
            "API Description",
            "API Version",
            "Terms of service",
            new Contact("Name", "www.example.com", "contact@example.com"),
            "License of API", "API license URL");
    }
	
}
