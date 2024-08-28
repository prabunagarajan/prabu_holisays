package com.devar.cabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
////@EnableEurekaClient
@ComponentScan("com.devar.*")
@EntityScan("com.devar.*")
//@EnableJpaRepositories("com.devar.*")
@EnableJpaAuditing
//@Log4j2
//@EnableDiscoveryClient
//@EnableAsync
//@RefreshScope
//@EnableScheduling
//@EnableCaching
public class DevarCabsApplication {

	
//	@Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	public static void main(String[] args) {
		SpringApplication.run(DevarCabsApplication.class, args);
	}
	

	
}
