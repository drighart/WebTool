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

import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/page")
public class PageRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PageRest.class);

	@GET
    @Path("get/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getPages(@PathParam("id") Integer id) {
		Page page = PageRepository.find(id);
		return Response.status(Response.Status.OK).entity(page).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(Page page) {
		LOGGER.debug("Update page with id {}, name '{}' and title '{}'", page.getId(), page.getName(), page.getTitle());
		
		PageRepository.update(page.getId(), page.getTitle());
		return Response.status(Response.Status.OK).build();
	}

	@POST
    @Path("new")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response newPage(Page page) {
		LOGGER.debug("New page with name '{}' and title '{}'", page.getName(), page.getTitle());
		
		PageRepository.insert(page.getName(), page.getTitle());
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("id") Integer id) {
		LOGGER.debug("Remove page with id '{}'", id);
		
		PageRepository.remove(id);
		return Response.status(Response.Status.OK).build();
	}

}
