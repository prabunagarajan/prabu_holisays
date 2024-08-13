// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.conf;

import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ApiInfo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//	@Bean
//	public Docket productApi() {
//		final ParameterBuilder aParameterBuilder = new ParameterBuilder();
//		aParameterBuilder.name("X-Authorization").modelRef((ModelReference) new ModelRef("string"))
//				.parameterType("header").defaultValue("").required(false).build();
//
//		final List<Parameter> aParameters = new ArrayList<Parameter>();
//
//		aParameters.add(aParameterBuilder.build());
//		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.oasys"))
//				.paths(PathSelectors.regex("/.*")).build().pathMapping("").globalOperationParameters(aParameters)
//				.apiInfo(this.metaData());
//	}
//
//	private ApiInfo metaData() {
//		final ApiInfo apiInfo = new ApiInfo("UP Excise Department", "HELPDESK - DATA API", "1.0", "Terms of service",
//				new Contact("", "", ""), "", "");
//		return apiInfo;
//	}

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket productApi() {
		final ParameterBuilder aParameterBuilder = new ParameterBuilder();
		aParameterBuilder.name("X-Authorization").modelRef((ModelReference) new ModelRef("string"))
				.parameterType("header").defaultValue("").required(false).build();
		final List<Parameter> aParameters = new ArrayList<Parameter>();
		aParameters.add(aParameterBuilder.build());
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.oasys"))
				.paths(PathSelectors.any()).build().apiInfo(this.metaData())
				.globalOperationParameters((List) aParameters);
	}

	private ApiInfo metaData() {
		final ApiInfo apiInfo = new ApiInfo("IESCMS", "Oasys Excise Department Application", "1.0", "Terms of service",
				new Contact("IESCMS", "http://www.oasys.co/index.html", ""), "Apache 2.x", "https://www.apache.org/licenses/LICENSE-2.0.html");
		return apiInfo;
	}

}
