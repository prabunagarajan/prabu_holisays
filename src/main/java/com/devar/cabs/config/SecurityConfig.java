
package com.devar.cabs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.devar.cabs.service.UserService;
import com.devar.cabs.utility.JwtRequestFilter;

@EnableWebSecurity
@Configuration
@Order(1) // Ensure this is unique if there are other security configurations
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	  @Autowired
	    private JwtRequestFilter jwtRequestFilter;

	    @Autowired
	    private UserService userService;

	    @Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	    }

	    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.cors().and().csrf().disable()
	            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	            .authorizeRequests()
	            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
	            .antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
	            .antMatchers("/authentication/**", "/actuator/info/**", "/actuator/**", "/v2/api-docs",
						"/swagger-resources", "/swagger-resources/**", "/validatorUrl", "/swagger-ui.html",
						"/webjars/**", "/hystrix/**", "/hystrix", "*.stream", "/hystrix.stream", "/proxy.stream",
						"/autuator/refresh", "/refresh", "/refresh/**")
	            .permitAll().anyRequest().authenticated().and().logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();

	        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	    }

	    @Override
	    @Bean(BeanIds.AUTHENTICATION_MANAGER)
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder(8);
	    }
}
