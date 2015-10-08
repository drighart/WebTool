package org.drdevelopment.webtool.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.HealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
@Path("/health")
public class HealthRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(HealthRest.class);
	
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
    public Response getStatus() {
    	if (HealthService.getHealthService().getStatus()) {
    		return Response.status(Response.Status.OK).build();
    	} else {
    		Exception exception = HealthService.getHealthService().getStatusException();
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception == null ? "" : exception.getMessage()).build();
    	}
    }
    
}