package org.drdevelopment.webtool.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class JacksonProducer extends JacksonJsonProvider implements ContextResolver<ObjectMapper> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JacksonProducer.class);

	private final ObjectMapper json;

    public JacksonProducer() throws Exception {
        this.json = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JSR310Module())
            .configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
    }

    @Override
    public ObjectMapper getContext( Class<?> objectType ) {
        return json;
    }

}
