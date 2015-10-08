package org.drdevelopment.webtool.plugin.api;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface PluginHttpServlet extends ExtensionPoint, Servlet, ServletConfig, java.io.Serializable {

	String getContextPath();
	
    void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
