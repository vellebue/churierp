package org.bastanchu.churierp.churierpweb.core;

import com.vaadin.flow.spring.VaadinMVCWebAppInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.Collection;

@Configuration
public class WebAppInitializer extends VaadinMVCWebAppInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
                .addMappingForUrlPatterns(null, false, "/*");
    }

    @Override
    protected Collection<Class<?>> getConfigurationClasses() {
        // Add configuration classes you need here (datasources...)
        return Arrays.asList(BaseConfiguration.class, WebConfiguration.class, WebSecurityConfiguration.class);
    }
}
