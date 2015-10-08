package org.drdevelopment.webtool.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.User;
import org.drdevelopment.webtool.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/users")
public class UsersRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersRest.class);

	@GET
    @Path("list")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getUsers() {
		List<User> list = UsersRepository.getUsers();
		return Response.status(Response.Status.OK).entity(list).build();
	}

	
}
