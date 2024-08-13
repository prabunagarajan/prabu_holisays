// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.conf.filter;

import org.springframework.context.annotation.Bean;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config.slf4jfilter")
public class Slf4jMDCFilterConfiguration
{
    public static final String DEFAULT_RESPONSE_TOKEN_HEADER = "Response_Token";
    public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "Slf4jMDCFilter.UUID";
    private String responseHeader;
    private String mdcTokenKey;
    private String requestHeader;
    
    public Slf4jMDCFilterConfiguration() {
        this.responseHeader = "Response_Token";
        this.mdcTokenKey = "Slf4jMDCFilter.UUID";
        this.requestHeader = null;
    }
    
    @Bean
    public FilterRegistrationBean servletRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        final Slf4jMDCFilter log4jMDCFilterFilter = new Slf4jMDCFilter(this.responseHeader, this.mdcTokenKey, this.requestHeader);
        registrationBean.setFilter((Filter)log4jMDCFilterFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
