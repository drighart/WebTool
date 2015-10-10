package org.drdevelopment.webtool.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.drdevelopment.webtool.rest.HealthRest;
import org.drdevelopment.webtool.rest.JacksonProducer;
import org.drdevelopment.webtool.rest.LoginRest;
import org.drdevelopment.webtool.rest.RestExceptionHandler;
import org.drdevelopment.webtool.rest.unsecured.ImageDisplayRest;
import org.drdevelopment.webtool.rest.unsecured.SetupRest;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataReader;
import org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataWriter;
import org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationReader;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationWriter;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInputImpl;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataReader;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsecuredMessageApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsecuredMessageApplication.class);

    private static Set<Class<?>> services = null;

    private synchronized static Set<Class<?>> getServices() {
    	if (services == null) {
    		services = new HashSet<>();
    		
    		services.add(RestExceptionHandler.class);
    		services.add(JacksonProducer.class);
    		
    		services.add(MultipartFormDataInputImpl.class);
    		services.add(MultipartFormDataReader.class);
    		services.add(MultipartFormDataWriter.class);
    		services.add(MimeMultipartProvider.class);
    		services.add(MapMultipartFormDataReader.class);
    		services.add(MapMultipartFormDataWriter.class);
    		services.add(MultipartFormAnnotationReader.class);
    		services.add(MultipartFormAnnotationWriter.class);

    		services.add(ResteasyJackson2Provider.class);
    		
       		services.add(ImageDisplayRest.class);
       		services.add(HealthRest.class);
       		services.add(SetupRest.class);
    		LOGGER.debug("Unsecure rest services registered.");
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