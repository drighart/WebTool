package org.drdevelopment.webtool.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.Preferences;
import org.drdevelopment.webtool.repository.PreferencesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/preferences")
public class PreferenceRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PreferenceRest.class);

	@GET
    @Path("get")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getPreferences() {
		Preferences preferences = PreferencesRepository.getPreferences();
		return Response.status(Response.Status.OK).entity(preferences).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(Preferences preferences) {
		LOGGER.debug("Update preferences");
		PreferencesRepository.update(preferences);
		return Response.status(Response.Status.OK).build();
	}

}
