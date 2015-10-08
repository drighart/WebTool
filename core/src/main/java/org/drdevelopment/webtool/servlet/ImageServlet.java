package org.drdevelopment.webtool.servlet;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drdevelopment.webtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServlet.class);
    private String folder;
    
    public ImageServlet(String folder) {
        this.folder = folder;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	getImage(request, response, folder);
    }

    public static void getImage(HttpServletRequest request, HttpServletResponse response, String folder) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        
        String uri = request.getRequestURI();
        String fileExtension = getFileExtension(uri);
        String fullUri = getFullUri(folder, uri);
//    	LOGGER.debug("fullUri {}", fullUri);

        if (fileExtension == null || fileExtension.isEmpty()) {
        	LOGGER.warn("File extension not found in uri {}.", uri);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } else if (!FileUtil.isResourceExists(fullUri)) {
//        	LOGGER.warn("Resource could not be found {}.", fullUri);
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
        	String contentType = getContentType(fileExtension);
        	if (contentType == null || contentType.isEmpty()) {
        		LOGGER.warn("Content type image with file extension {} could not be defined.", fileExtension);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	} else if (FileUtil.isResourceExists(fullUri)) {
        		response.setContentType(contentType);
        		response.getOutputStream().write(FileUtil.readBytesFromResource(fullUri));
        		response.getOutputStream().close();
        	} else if (FileUtil.isFileExists(fullUri)) {
        		response.setContentType(contentType);
        		response.getOutputStream().write(FileUtil.readBytes(fullUri));
        		response.getOutputStream().close();
        	}	
////        		LOGGER.debug("Content type requested for image is {}.", contentType);
//        		response.setContentType(contentType);
//        		response.getOutputStream().write(FileUtil.readBytesFromResource(fullUri));
//        		response.getOutputStream().close();
//        	}
        }        
    }
    
    private static String getFullUri(String folder, String uri) {
    	String osUri = null;
    	String osFolder = null;
    	if (File.separatorChar == '\\') {
    		osUri = uri.replace("/", File.separator);
    		osFolder = folder.replace("/", File.separator);
    	} else {
    		osUri = uri.replace("\\", File.separator);
    		osFolder = folder.replace("\\", File.separator);
    	}
    	String newUrl = osFolder + (osUri != null && (osUri.startsWith(File.separator)) ? "" : File.separator) + osUri;
    	return newUrl;
    }

    private static String getFileExtension(String uri) {
    	if (uri != null && !uri.isEmpty()) {
    		int pos = uri.lastIndexOf('.');
    		if (pos < 0) {
    			LOGGER.warn("Could not find extension in uri {}.", uri);
    		} else {
    			return uri.substring(pos);
    		}
    	}
    	return null;
    }
    
    private static String getContentType(String fileExtension) {
    	String contentType = null;
    	if (FileExtenstionMediaType.IMAGE.isMediaType(fileExtension)) {
    		if (".ico".equalsIgnoreCase(fileExtension)) {
    			contentType = "image/x-icon";
    		} else {
    			contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileExtension);
    		}
    	}
        return contentType;
    }
}