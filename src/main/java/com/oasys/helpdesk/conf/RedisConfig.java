package com.oasys.helpdesk.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.port}")
  	private Integer port;
	
	@Value("${com.spring.redis.host}")
  	private String host;
	
	@Value("${spring.redis.password}")
	private String password;
	
//	@Bean
//	JedisConnectionFactory jedisConnectionFactory() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", port);
////		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
//		
//		 JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
//		 jedisConnectionFactory.getPoolConfig().setMaxIdle(30);
//		 jedisConnectionFactory.getPoolConfig().setMinIdle(10);
//		 jedisConnectionFactory.getPoolConfig().setMaxTotal(128);
//		 return jedisConnectionFactory;
//	}
//
//	@Bean
//	public RedisTemplate redisTemplate() {
//		RedisTemplate template = new RedisTemplate<>();
//		template.setConnectionFactory(jedisConnectionFactory());
//		template.setEnableTransactionSupport(true);
//		
//		return template;
//	}
}
