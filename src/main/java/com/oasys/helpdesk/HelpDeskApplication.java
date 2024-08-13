package com.oasys.helpdesk;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.oasys.helpdesk.conf.UserAuditAware;

import lombok.extern.log4j.Log4j2;
//import org.jasypt.digest.PooledStringDigester;
//import org.jasypt.digest.StringDigester;

@SpringBootApplication
//@EnableEurekaClient
@ComponentScan("com.oasys.*")
@EntityScan("com.oasys.*")
@EnableJpaRepositories("com.oasys.*")
@EnableJpaAuditing
@Log4j2
@EnableDiscoveryClient
@EnableAsync
@RefreshScope
@EnableScheduling
//@EnableCaching
public class HelpDeskApplication {

	
//	@Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	
	
	
//	@Bean
//	public RestTemplate restTemplate() {
//		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//		requestFactory.setReadTimeout(90000);
//		return new RestTemplate(requestFactory);
//	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
	
//	@Bean
//	StringDigester PasswordHash() {
//		final PooledStringDigester stringDigester = new PooledStringDigester();
//		stringDigester.setAlgorithm("SHA-256");
//		stringDigester.setIterations(1000);
//		stringDigester.setSaltSizeBytes(10);
//		stringDigester.setPoolSize(16);
//		stringDigester.initialize();
//		return stringDigester;
//
//	}
	
	/**
	 * @return
	 */
	@Bean
	public AuditorAware<Long> auditorAware() {
		UserAuditAware userAuditAware = new UserAuditAware();
		if (userAuditAware!=null && userAuditAware.getCurrentAuditor() != null) {
			log.info("current user >>>> " + userAuditAware.getCurrentAuditor());
		}
		return userAuditAware;
	}
	/**
	 * Async call execution to other services
	 * @return
	 */
	@Bean(name="processExecutorHelpdesk")
    public TaskExecutor workExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(600);
        threadPoolTaskExecutor.afterPropertiesSet();        
        return threadPoolTaskExecutor;
    }
	
	@Bean
	public MessageSource messageSource() {
		
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:messages/messages","classpath:messages/messages_en");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
		
	}
	

	@Bean
	public LocaleResolver localeResolver() {
	    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
	    localeResolver.setDefaultLocale(Locale.ENGLISH);
	    return localeResolver;
	}
	 
	
	

	
}
