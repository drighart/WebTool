package org.drdevelopment.webtool.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedirectServlet.class);
	private String redirect;
	
	public RedirectServlet(String redirect) {
		super();
		this.redirect = redirect;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);

		response.sendRedirect(redirect);
	}

}