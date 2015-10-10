package org.drdevelopment.webtool.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.drdevelopment.webtool.rest.ConfigurationItemRest;
import org.drdevelopment.webtool.rest.ImageRest;
import org.drdevelopment.webtool.rest.ImagesRest;
import org.drdevelopment.webtool.rest.JacksonProducer;
import org.drdevelopment.webtool.rest.LoginRest;
import org.drdevelopment.webtool.rest.MenuItemRest;
import org.drdevelopment.webtool.rest.MenuItemsRest;
import org.drdevelopment.webtool.rest.OverviewRest;
import org.drdevelopment.webtool.rest.PageRest;
import org.drdevelopment.webtool.rest.PagesRest;
import org.drdevelopment.webtool.rest.ParagraphRest;
import org.drdevelopment.webtool.rest.RestExceptionHandler;
import org.drdevelopment.webtool.rest.SystemRest;
import org.drdevelopment.webtool.rest.TestServiceRest;
import org.drdevelopment.webtool.rest.UserRest;
import org.drdevelopment.webtool.rest.UsersRest;
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

public class SecureMessageApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecureMessageApplication.class);

    private static Set<Class<?>> services = null;
    
    private synchronized static Set<Class<?>> getServices() {
    	if (services == null) {
    		LOGGER.debug("Jackson: create services");
    		services = new HashSet<>();
    		services.add(TestServiceRest.class);

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
    		
    		services.add(OverviewRest.class);
    		services.add(SystemRest.class);
    		services.add(ConfigurationItemRest.class);
       		services.add(PagesRest.class);
       		services.add(PageRest.class);
       		services.add(ParagraphRest.class);
       		services.add(MenuItemsRest.class);
       		services.add(MenuItemRest.class);
       		services.add(ImageRest.class);
       		services.add(ImagesRest.class);
       		services.add(UsersRest.class);
       		services.add(UserRest.class);
    		services.add(LoginRest.class);
    		
    		LOGGER.debug("Secure rest services registered.");
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