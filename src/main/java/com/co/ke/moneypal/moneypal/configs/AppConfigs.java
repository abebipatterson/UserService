package com.co.ke.moneypal.moneypal.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class AppConfigs {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private ApiInfo apiInfo() {
       // http://localhost:6666/swagger-ui.html#
        return new ApiInfo(
                "MONEYPAL USERSERVICE API",
                "All REST APIs for MONEYPAL USER SERVICE",
                "1.0",
                "@MoneyPal 2021 Terms of service",
                new Contact("Nelson Otieno", "www.moneypal.com", "nelson62moses@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    @Bean
    public Docket setSwagger(){
        return new Docket(DocumentationType.SWAGGER_2)
               // .apiInfo(apiInfo())
                //.securityContexts(Arrays.asList(securityContext()))
                //.securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
