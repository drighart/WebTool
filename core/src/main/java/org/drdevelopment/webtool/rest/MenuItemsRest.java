package org.drdevelopment.webtool.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.MenuItem;
import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.repository.MenuItemsRepository;
import org.drdevelopment.webtool.repository.PagesRepository;
import org.drdevelopment.webtool.rest.data.ListMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/menuitems")
public class MenuItemsRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemsRest.class);

	@GET
    @Path("list")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getMenuItems() {
		List<MenuItem> list = MenuItemsRepository.getMenuItems();
		Map<Integer, Page> pages = PagesRepository.getPagesAsMap();
		
		List<ListMenuItem> output = new ArrayList<>();
		for (MenuItem menuItem : list) {
			Page page = pages.get(menuItem.getPageId());
			output.add(new ListMenuItem(menuItem.getPosition(), menuItem.getName(), page == null ? null : page.getName()));
		}
		return Response.status(Response.Status.OK).entity(output).build();
	}

	@POST
    @Path("new")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response newMenuItem(MenuItem menuItem) {
		LOGGER.debug("New menu-item with name '{}'.", menuItem.getName());
		
		MenuItemsRepository.insert(menuItem.getName());
		return Response.status(Response.Status.OK).build();
	}

	@PUT
    @Path("up/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response up(@PathParam("position") Integer position) {
		LOGGER.debug("Moving up menu-item with position '{}'", position);
		
		MenuItemsRepository.up(position);
		return Response.status(Response.Status.OK).build();
	}

	@PUT
    @Path("down/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response down(@PathParam("position") Integer position) {
		LOGGER.debug("Moving down menu-item with position '{}'", position);
		
		MenuItemsRepository.down(position);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("name") String name) {
		LOGGER.debug("Remove menu-item with name '{}'", name);
		
		MenuItemsRepository.remove(name);
		return Response.status(Response.Status.OK).build();
	}

}
