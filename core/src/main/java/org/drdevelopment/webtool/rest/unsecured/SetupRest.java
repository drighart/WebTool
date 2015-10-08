package org.drdevelopment.webtool.rest.unsecured;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.User;
import org.drdevelopment.webtool.repository.ConfigurationItemRepository;
import org.drdevelopment.webtool.repository.UserRepository;
import org.drdevelopment.webtool.repository.UsersRepository;
import org.drdevelopment.webtool.rest.data.WebsiteSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/setup")
public class SetupRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SetupRest.class);
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response setupWebsite(WebsiteSetup setup) {
		List<User> users = UsersRepository.getUsers();
		if (users == null || users.isEmpty()) {
			LOGGER.debug("Setup website with name '{}'.", setup.getName());

			UserRepository.insert(setup.getEmailAddress(), "admin", "", setup.getLanguage());
			UserRepository.changeCredentials(setup.getEmailAddress(), setup.getPassword());
			UserRepository.activate(setup.getEmailAddress());
			ConfigurationItemRepository.update("Website name", setup.getName());
			
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}

