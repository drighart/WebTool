package org.drdevelopment.webtool.plugin.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drdevelopment.webtool.plugin.api.PluginHttpServlet;
import org.drdevelopment.webtool.plugin.authentication.User;
import org.drdevelopment.webtool.plugin.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class UsersServiceExtension extends HttpServlet implements PluginHttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersServiceExtension.class);
//	private ObjectMapper mapper = new ObjectMapper();
	private UsersService service = new UsersService();

	@Override
	public String getContextPath() {
		return "/users/*";
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        
        List<User> users = service.getUsers();
        
//        try {
//			service.addUser("admin", "david" + System.currentTimeMillis(), "david", "", "");
//		} catch (PluginException e) {
//			LOGGER.error(e.getMessage(), e);
//		}
        
        LOGGER.info("Found {} users.", users.size());
        
//        mapper.writeValue(response.getWriter(), users);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
