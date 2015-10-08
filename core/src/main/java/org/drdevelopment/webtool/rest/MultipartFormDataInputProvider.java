package org.drdevelopment.webtool.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInputImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Provider
//@Produces( MediaType.APPLICATION_JSON )
//@Consumes( MediaType.APPLICATION_JSON )
public class MultipartFormDataInputProvider extends MultipartFormDataInputImpl implements MultipartFormDataInput {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultipartFormDataInputProvider.class);

	public MultipartFormDataInputProvider(MediaType contentType, Providers workers) {
		super(contentType, workers);
		LOGGER.debug("Using provider MultipartFormDataInputProvider");
	}

}
