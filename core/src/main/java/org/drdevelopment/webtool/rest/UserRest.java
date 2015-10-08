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

import org.drdevelopment.webtool.model.User;
import org.drdevelopment.webtool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/user")
public class UserRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRest.class);

	@GET
    @Path("get/{emailAddress}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getUsers(@PathParam("emailAddress") String emailAddress) {
		User user = UserRepository.find(emailAddress);
		user.setCredentials(null);
		return Response.status(Response.Status.OK).entity(user).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(User user) {
		LOGGER.debug("Update user with emailAddress {}", user.getEmailAddress());
		
		UserRepository.update(user.getEmailAddress(), user.getRoles(), user.getDisplayName());
		return Response.status(Response.Status.OK).build();
	}

	@POST
    @Path("new")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response newUser(User user) {
		LOGGER.debug("New user with emailAddress {}", user.getEmailAddress());
		
		UserRepository.insert(user.getEmailAddress(), user.getRoles(), user.getDisplayName(), user.getLanguage());
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{emailAddress}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("emailAddress") String emailAddress) {
		LOGGER.debug("Remove user with emailAddress '{}'", emailAddress);
		
		UserRepository.remove(emailAddress);
		return Response.status(Response.Status.OK).build();
	}

}
