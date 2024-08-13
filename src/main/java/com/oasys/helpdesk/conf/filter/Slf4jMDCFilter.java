// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.conf.filter;

import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.jboss.logging.MDC;
import java.util.UUID;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class Slf4jMDCFilter extends OncePerRequestFilter
{
    private String responseHeader;
    private String mdcTokenKey;
    private String requestHeader;
    
    public Slf4jMDCFilter() {
    }
    
    public Slf4jMDCFilter(final String responseHeader, final String mdcTokenKey, final String requestHeader) {
        this.responseHeader = responseHeader;
        this.mdcTokenKey = mdcTokenKey;
        this.requestHeader = requestHeader;
    }
    
    public String getResponseHeader() {
        return this.responseHeader;
    }
    
    public String getMdcTokenKey() {
        return this.mdcTokenKey;
    }
    
    public String getRequestHeader() {
        return this.requestHeader;
    }
    
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            String token;
            if (!StringUtils.isEmpty((Object)this.requestHeader) && !StringUtils.isEmpty((Object)request.getHeader(this.requestHeader))) {
                token = request.getHeader(this.requestHeader);
            }
            else {
                token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
            }
            MDC.put(this.mdcTokenKey, (Object)token);
            if (!StringUtils.isEmpty((Object)this.responseHeader)) {
                response.addHeader(this.responseHeader, token);
            }
            chain.doFilter((ServletRequest)request, (ServletResponse)response);
        }
        finally {
            MDC.remove(this.mdcTokenKey);
        }
    }
}
