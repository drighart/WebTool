package org.drdevelopment.webtool.plugin;

import java.io.IOException;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drdevelopment.webtool.plugin.api.PluginHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class ExampleServletExtension extends HttpServlet implements PluginHttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleServletExtension.class);

	@Override
	public String getContextPath() {
		return "/hello/world/*";
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long ms = System.currentTimeMillis();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello World!</h1><br/>");
        response.getWriter().println("session = " + request.getSession(true).getId() + "<br/>");
        response.getWriter().println("current time = " + LocalTime.now() + "<br/>");
        LOGGER.info("Call returned in {}ms.", (System.currentTimeMillis() - ms));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
