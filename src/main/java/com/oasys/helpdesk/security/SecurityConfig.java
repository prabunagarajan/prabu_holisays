package com.oasys.helpdesk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.oasys.helpdesk.service.CustomerDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomerDetailsService customerService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customerService).passwordEncoder(passwordEncoder());
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(HttpMethod.POST, "/device-registration").permitAll()
				.antMatchers(HttpMethod.GET, "/device/is-associated").permitAll()
				.antMatchers(HttpMethod.PUT, "/ticket/escalate").permitAll()
				.antMatchers(HttpMethod.POST, "/device-registration/upload").permitAll()
				.antMatchers(HttpMethod.GET, "/device/getShopCode").permitAll()
				.antMatchers(HttpMethod.GET, "/versionmanagement/latestVersion").permitAll()
				.antMatchers(HttpMethod.POST, "/user/logout").permitAll()
				.antMatchers(HttpMethod.PUT, "/device-registration/mapping").permitAll()

				// newly
				// added///////////////////////////////////////////////////////////////////
				.antMatchers(HttpMethod.GET, "/masterchangerequestfeatures/activelist").permitAll()
				.antMatchers(HttpMethod.POST, "/changerequest/add").permitAll()
				.antMatchers(HttpMethod.PUT, "/changerequest/approval").permitAll()
				.antMatchers(HttpMethod.POST, "/changerequest/search").permitAll()
				.antMatchers(HttpMethod.GET, "/changerequest/getById").permitAll()
				.antMatchers(HttpMethod.PUT, "/changerequest/update").permitAll()
				.antMatchers(HttpMethod.POST, "/changerequest/updateWorkFlow").permitAll()
				.antMatchers(HttpMethod.PUT, "/changerequest/changereqstatusupdate").permitAll()
				.antMatchers(HttpMethod.GET, "/ticketcategory/active").permitAll()
				.antMatchers(HttpMethod.GET, "/ticketstatus/allactive").permitAll()
				.antMatchers(HttpMethod.GET, "/issuefrom/active").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/listlicensenrViaApp").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/paymentappcount").permitAll()
				.antMatchers(HttpMethod.GET, "/role-master/getAll").permitAll()
				.antMatchers(HttpMethod.GET, "/devicelost/getListByUserId").permitAll()
				.antMatchers(HttpMethod.GET, "/devicereturn/getAllByUserId").permitAll()
				.antMatchers(HttpMethod.GET, "/asset-type/allactive").permitAll()
				.antMatchers(HttpMethod.GET, "/assetaccessories/allactive").permitAll()
				.antMatchers(HttpMethod.GET, "devicereturn/getApplicationNo").permitAll()
				.antMatchers(HttpMethod.GET, "devicedamage/getListByUserId").permitAll()
				.antMatchers(HttpMethod.GET, "/devicedamage/getApplicationNo").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstockoverview/stockoverviewsearch").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstockoverview/mappedcodes").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstockoverview/unmappedcodes").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/search").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/ealdasboardapplicant").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/add").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/districtwiseentityticket").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/code").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/code").permitAll()
				.antMatchers(HttpMethod.GET, "/devicelost/getApplicationNo").permitAll()
				.antMatchers(HttpMethod.GET, "/ticket/dashboardbymonth").permitAll()
				.antMatchers(HttpMethod.GET, "/ticket/dashboardcount").permitAll()
				.antMatchers(HttpMethod.GET, "/ticket/getById").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/activity-logs").permitAll()
				.antMatchers(HttpMethod.GET, "/surveyform/getAllQuestions").permitAll()
				.antMatchers(HttpMethod.POST, "/commonmaster/licencemanagement").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/paymentappsearch").permitAll()
				.antMatchers(HttpMethod.GET, "/devicelost/getListByUserId").permitAll()
				.antMatchers(HttpMethod.POST, "/devicelost/search").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/ealdasboard").permitAll()
				.antMatchers(HttpMethod.GET, "/surveyform/getDatabytcNo").permitAll()
				.antMatchers(HttpMethod.PUT, "/ticket/updatedescription").permitAll()
				.antMatchers(HttpMethod.GET, "/ticketsubcategory/getSubCategoryByCategoryId").permitAll()
				.antMatchers(HttpMethod.PUT, "/devicelost/verifylicnocomplaintno").permitAll()
				.antMatchers(HttpMethod.POST, "/devicereturn/search").permitAll()
				.antMatchers(HttpMethod.GET, "/devicereturn/getById").permitAll()
				.antMatchers(HttpMethod.GET, "/devicereturn/getLogsByApplicationNo").permitAll()
				.antMatchers(HttpMethod.POST, "/device/fpscodemulti/search").permitAll()
				.antMatchers(HttpMethod.POST, "/assetmap/search").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstock/search").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getstock").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/geteal").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/opnstockcode").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstock/openingstockadd").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/getById").permitAll()
				.antMatchers(HttpMethod.GET, "/devicedamage/getById").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getLogsByApplicationNo").permitAll()
				.antMatchers(HttpMethod.POST, "/devicedamage/add").permitAll()
				.antMatchers(HttpMethod.GET, "/issuedetails/getIssueDetails").permitAll()
				.antMatchers(HttpMethod.GET, "/priority/get/{subCategoryId}/{categoryId}").permitAll()
				.antMatchers(HttpMethod.PUT, "/devicedamage/update").permitAll()
				.antMatchers(HttpMethod.POST, "/devicelost/add").permitAll()
				.antMatchers(HttpMethod.POST, "/devicereturn/add").permitAll()
				.antMatchers(HttpMethod.POST, "/changerequest/changereqcount").permitAll()
				.antMatchers(HttpMethod.GET, "/changerequest/summarycount").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/listlicensenrViaApppagination").permitAll()
				.antMatchers(HttpMethod.POST, "/helpdeskfaq/search").permitAll()
				.antMatchers(HttpMethod.GET, "/issuedetails/getIssueDetails/{subCategoryId}/{categoryId}").permitAll()
				.antMatchers(HttpMethod.POST, "/device/search").permitAll()
				.antMatchers(HttpMethod.GET, "/asset-type/active").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getdiapatcheddetails").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/opnstockcode").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/getByeal").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/getByealsub").permitAll()
				.antMatchers(HttpMethod.GET, "/sla/getSla/{categoryId}/{subcategoryId}/{issueDetailsId}").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/applicationviappadd").permitAll()
				.antMatchers(HttpMethod.GET, "/changerequest/getLogsByApplicationNo/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/paymentappsearchnew").permitAll()
				.antMatchers(HttpMethod.POST, "/changerequest/draftcall").permitAll()
				.antMatchers(HttpMethod.POST, "/grievancereg/codetypeofuser").permitAll()
				.antMatchers(HttpMethod.POST, "/grievanceCategory/activetypeofuser").permitAll()
				.antMatchers(HttpMethod.POST, "/ticket/allticketdownload").permitAll()
				.antMatchers(HttpMethod.POST, "/grievancereg/toglelist").permitAll()
				.antMatchers(HttpMethod.GET, "/devicedamage/getListByDesignationCode").permitAll()
				.antMatchers(HttpMethod.PUT, "/devicedamage/approval").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/getById/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/GrievanceIssueDetails/grivanceissuedetails").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/getByealrequest/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/ealavailability").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getLogsByApplicationNo/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealrequest/approval").permitAll()
				.antMatchers(HttpMethod.GET, "/bwfldispatchrequest/bwflgetById/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/bwfldispatchrequest/bwfldispatchadd").permitAll()
				.antMatchers(HttpMethod.GET, "/eal/wastage/inprogresslist").permitAll()
				.antMatchers(HttpMethod.POST, "/eal/wastage/list").permitAll()
				.antMatchers(HttpMethod.GET, "/eal/wastage/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/eal/wastage").permitAll()
				.antMatchers(HttpMethod.PUT, "/eal/wastage/{id}").permitAll()
				.antMatchers(HttpMethod.PUT, "/eal/wastage/status/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequestputobwfl/add").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealrequestputobwfl/update").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getById/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getByealrequest/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getlist").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequestputobwfl/search").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getAllByUserId/{userId}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getLogsByApplicationNo/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealrequestputobwfl/approval").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequestputobwfl/forceclosure").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequestputobwfl/getAllByapproved").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstock/pu/add").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getById/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getByealrequest/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getlist").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/code").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstock/pu/search").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getAllByUserId/{userId}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getLogsByApplicationNo/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealstock/pu/update").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getealrequet/{applicationNo}/{createdby}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getstock/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/geteal/{ealrequestApplnno}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getstockavailable/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getdiapatcheddetails/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.POST, "/bwfldispatchrequest/Bwflsearch").permitAll()
				.antMatchers(HttpMethod.POST, "/bwfldispatchrequest/bwflvendorstatusupdate").permitAll()
				.antMatchers(HttpMethod.GET, "/tprequest/code").permitAll()
				.antMatchers(HttpMethod.GET,"/pdfgen/tpfile/{tpNumber}").permitAll()
				.antMatchers(HttpMethod.GET,"/pdfgen/ealtpfile/{tpNumber}").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/aec/add").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealrequest/aec/update").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getById/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getByealrequest/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getlist").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/aec/search").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getAllByUserId/{userId}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getLogsByApplicationNo/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.PUT, "/ealrequest/aec/approval").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/aec/forceclosure").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getAllByapproved").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getLogsByApplicationNo").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/aec/getByAppliNo").permitAll()
				.antMatchers(HttpMethod.POST, "/eal/wastage/updateWastageWorkFlow").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getealrequet").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getealrequet/{applicationNo}/{createdby}").permitAll()
				.antMatchers(HttpMethod.POST, "/ealstock/add").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getAllByUserId/{userId}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getstock/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/geteal/{ealrequestApplnno}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getstockavailable/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getdiapatcheddetails/{applicationNo}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/getdiapatcheddetailstp/{tpApplnno}").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/mapStockSummary").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/unmapStockSummary").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/puMapStockSummary").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstockoverview/puUnmapStockSummary").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/updateWorkFlow").permitAll()
				.antMatchers(HttpMethod.GET,"/pdfgen/ealSlip").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/getVendorRollCode").permitAll()
				.antMatchers(HttpMethod.GET, "/ealrequest/getVendorUserId").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/filtersearch").permitAll()
				.antMatchers(HttpMethod.GET, "/tprequest/balance/{tpApplnno}").permitAll()
				.antMatchers(HttpMethod.POST, "/ealrequest/update-vendor-id").permitAll()
				.antMatchers(HttpMethod.GET, "/ealstock/pu/getealrequet/{applicationNo}/{createdby}").permitAll()
				

				///////////////////////////////////////////////////
				.antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html",
						"/**/*.css", "/**/*.js")
				.permitAll()
				.antMatchers("/authentication/**", "/actuator/info/**", "/actuator/**", "/v2/api-docs",
						"/swagger-resources", "/swagger-resources/**", "/validatorUrl", "/swagger-ui.html",
						"/webjars/**", "/hystrix/**", "/hystrix", "*.stream", "/hystrix.stream", "/proxy.stream",
						"/autuator/refresh", "/refresh", "/refresh/**")
				.permitAll().anyRequest().authenticated().and().logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
		// Add our custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		// "/activity/**","/appModule/**","/designation/**","/roleMaster/**","/usermanager/**"
	}

	/*
	 * @Override public void configure(WebSecurity web) throws Exception {
	 * web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
	 * "/swagger-resources/**", "/configuration/**","/swagger-ui.html",
	 * "/actuator/info/**","/actuator/**","/validatorUrl","/hystrix/**","/hystrix",
	 * "*.stream","/hystrix.stream","/proxy.stream",//newly added
	 * "/autuator/refresh","/refresh","/refresh/**","/update-reason",
	 * "/employment-status",//newly added "/webjars/**");
	 * 
	 * }
	 */
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS).
	 * permitAll().anyRequest()
	 * .authenticated().and().sessionManagement().sessionCreationPolicy(
	 * SessionCreationPolicy.STATELESS);
	 * 
	 * http.addFilterBefore(jwtRequestFilter,
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * }
	 * 
	 * @Bean public AuthenticationManager AuthenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 */

	
}
