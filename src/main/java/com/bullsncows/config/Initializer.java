package com.bullsncows.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.register(WebMvcConfig.class, SecurityConfig.class, JpaConfig.class);
        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(root));
        root.setServletContext(servletContext);
        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(root));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);
    }
}