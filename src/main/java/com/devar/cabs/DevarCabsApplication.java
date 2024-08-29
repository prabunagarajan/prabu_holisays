package com.devar.cabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@ComponentScan("com.devar.*")
@EntityScan("com.devar.*")
@EnableJpaAuditing

public class DevarCabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevarCabsApplication.class, args);
	}
	

	
}
