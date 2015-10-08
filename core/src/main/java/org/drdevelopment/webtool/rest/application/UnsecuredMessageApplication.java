package org.drdevelopment.webtool.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.drdevelopment.webtool.rest.HealthRest;
import org.drdevelopment.webtool.rest.LoginRest;
import org.drdevelopment.webtool.rest.unsecured.ImageDisplayRest;
import org.drdevelopment.webtool.rest.unsecured.SetupRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsecuredMessageApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsecuredMessageApplication.class);

    private static Set<Class<?>> services = null;

    private synchronized static Set<Class<?>> getServices() {
    	if (services == null) {
    		services = new HashSet<>();
       		services.add(ImageDisplayRest.class);
       		services.add(HealthRest.class);
       		services.add(SetupRest.class);
    	}
    	return services;
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        return getServices();
    }
    
    public static synchronized void addRestServices(Set<Class<?>> restServices) {
    	getServices().addAll(restServices);    	
    }
    
}