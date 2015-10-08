package org.drdevelopment.webtool.mail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.junit.Test;

public class TestFormbasedAuthentication {

	@Test
	public void test() throws Exception {
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);

		context.addServlet(new ServletHolder(new DefaultServlet() {
		  @Override
		  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    response.getWriter().append("hello " + request.getUserPrincipal().getName());
		  }
		}), "/*");

		context.addServlet(new ServletHolder(new DefaultServlet() {
		  @Override
		  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    response.getWriter().append("<html><form method='POST' action='/j_security_check'>"
		      + "<input type='text' name='j_username'/>"
		      + "<input type='password' name='j_password'/>"
		      + "<input type='submit' value='Login'/></form></html>");
		    }
		}), "/login");

		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__FORM_AUTH);
		constraint.setRoles(new String[]{"user","admin","moderator"});
		constraint.setAuthenticate(true);

		ConstraintMapping constraintMapping = new ConstraintMapping();
		constraintMapping.setConstraint(constraint);
		constraintMapping.setPathSpec("/*");

		ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
		securityHandler.addConstraintMapping(constraintMapping);
		HashLoginService loginService = new HashLoginService();
		loginService.putUser("username", new Password("password"), new String[] {"user"});
		securityHandler.setLoginService(loginService);

		FormAuthenticator authenticator = new FormAuthenticator("/login", "/login", false);
		securityHandler.setAuthenticator(authenticator);

		context.setSecurityHandler(securityHandler);

		server.start();
		server.join();	
	}

}
