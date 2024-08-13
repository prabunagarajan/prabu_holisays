package com.oasys.helpdesk.conf;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.oasys.helpdesk.constant.ResponseMessageConstant;

@Configuration
public class AppConfig {
	@Value("${readTimeout}")
	private String readTimeout;
	@Value("${connectionTimeout}")
	private String connectionTimeout;
	
	@Autowired
	private MessageSource messageSource;

	// private int timeout=1000;
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(Integer.parseInt(connectionTimeout)))
				.setReadTimeout(Duration.ofSeconds(Integer.parseInt(readTimeout))).build();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
	
	@PostConstruct
	public void init()
	{
		for(ResponseMessageConstant errorCode:ResponseMessageConstant.values())
		{
			errorCode.setMessageSource(messageSource);
		}
	}
	

}