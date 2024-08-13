// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.conf;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.oasys.helpdesk.HelpDeskApplication;

public class ServletInitializer extends SpringBootServletInitializer
{
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(new Class[] { HelpDeskApplication.class });
    }
}
