package org.drdevelopment.webtool.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/login")
public class LoginRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginRest.class);

	@Context 
	private HttpServletRequest request;
    
	@Context 
    private HttpServletResponse response;
	
	@Context
	private SecurityContext securityContext;
	
	@GET
    @Path("hello")
    @Produces({ MediaType.APPLICATION_JSON })
    public Principal hello() {
        return securityContext.getUserPrincipal();
    }
	
//	@POST
//    @Path("check22")
//	@Consumes({ MediaType.APPLICATION_JSON })
//    public Response check(LoginCredentials loginCredentials) {
//		if (loginCredentials != null && loginCredentials.getPassword() != null && !loginCredentials.getPassword().isEmpty()) {
//			User user = UserRepository.find(loginCredentials.getEmailAddress());
//			if (user == null) {
//				LOGGER.info("User {} not found!", loginCredentials.getEmailAddress());
//				return Response.status(Response.Status.BAD_REQUEST).build();
//			} else {
//				if (user.isActivated() && user.getCredentials().equals(loginCredentials.getPassword())) {
//					LOGGER.debug("User {} logged in.", user.getEmailAddress());
//					user.setCredentials(null);
//					user.setCreated(null);
//					user.setModified(null);
//					return Response.status(Response.Status.OK).entity(user).build();
//				} else {
//					if (!user.isActivated()) {
//						LOGGER.info("User {} is not activated and can not login.", user.getEmailAddress());
//					} else {
//						LOGGER.info("Incorrect password for user {}.", user.getEmailAddress());
//					}
//					return Response.status(Response.Status.BAD_REQUEST).build();
//				}
//			}
//		} else {
//			return Response.status(Response.Status.BAD_REQUEST).build();
//		}
//	}
}
