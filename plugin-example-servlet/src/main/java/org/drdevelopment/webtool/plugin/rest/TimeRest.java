package org.drdevelopment.webtool.plugin.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/time")
public class TimeRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeRest.class);

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getCurrentTime() {
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LOGGER.debug("Rest service called to retrieve the current time, which is {}.", now);

        return Response.status(Response.Status.OK).entity(now).build();
	}

}

