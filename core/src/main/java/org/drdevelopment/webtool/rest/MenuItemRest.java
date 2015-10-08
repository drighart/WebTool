package org.drdevelopment.webtool.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.MenuItem;
import org.drdevelopment.webtool.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/menuitem")
public class MenuItemRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemRest.class);

	@GET
    @Path("get/{position}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getMenuItem(@PathParam("position") Integer position) {
		MenuItem menuItem = MenuItemRepository.find(position);
		return Response.status(Response.Status.OK).entity(menuItem).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(MenuItem menuItem) {
		LOGGER.debug("Update menu-item with position {} and name '{}', onCurrentPage '{}' and page-id '{}'", 
				menuItem.getPosition(), menuItem.getName(), 
				menuItem.getOnCurrentPage(), menuItem.getPageId());
		
		MenuItemRepository.update(menuItem.getPosition(), menuItem.getName(), menuItem.getOnCurrentPage(), menuItem.getPageId());
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("position") Integer position) {
		LOGGER.debug("Remove menu-item with position '{}'", position);
		
		MenuItemRepository.remove(position);
		return Response.status(Response.Status.OK).build();
	}

}
