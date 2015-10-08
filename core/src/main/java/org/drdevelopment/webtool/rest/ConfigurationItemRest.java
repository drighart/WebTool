package org.drdevelopment.webtool.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.ConfigurationItem;
import org.drdevelopment.webtool.repository.ConfigurationItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/configurationitem")
public class ConfigurationItemRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationItemRest.class);

	@GET
    @Path("list")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getConfigurationItems() {
		List<ConfigurationItem> list = ConfigurationItemRepository.getConfigurationItems();
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
//	@RolesAllowed("ADMIN")
    public Response save(ConfigurationItem configurationItem) {
		LOGGER.debug("Update configuration item with name '{}' with value '{}'", configurationItem.getName(), configurationItem.getValue());
		ConfigurationItemRepository.update(configurationItem.getName(), configurationItem.getValue());
		return Response.status(Response.Status.OK).build();
	}

}
