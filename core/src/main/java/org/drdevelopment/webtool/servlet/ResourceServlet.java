package org.drdevelopment.webtool.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drdevelopment.webtool.rest.SystemRest;
import org.drdevelopment.webtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServlet.class);
    private String filename;
    
    public ResourceServlet(String filename) {
        this.filename = filename;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        readBinary(response, filename);
        
//        String source = "";
//        if (FileUtil.isFileExists(filename)) {
//        	source = FileUtil.readLinesFromResource(filename);
//        } else {
//        	LOGGER.warn("The file {} does not exist!", filename);
//        }
//        response.getWriter().println(source);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("j_username");
        String pass = request.getParameter("j_password");
        LOGGER.info("Check for user {} with password {}.", user, pass);
    }
    
	private void readBinary(HttpServletResponse response, String uri) {
		try {
			if (FileUtil.isResourceExists(uri)) {
				response.getOutputStream().write(FileUtil.readBytesFromResource(uri));
			} else if (FileUtil.isFileExists(uri)) {
				response.getOutputStream().write(FileUtil.readBytes(uri));
			} else {
				LOGGER.error("Can not find file or resource with uri {}.", uri);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch(IOException e) {
			LOGGER.debug("Could not find resource with uri {}", uri);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}