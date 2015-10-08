package org.drdevelopment.webtool.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drdevelopment.webtool.repository.UsersRepository;
import org.drdevelopment.webtool.template.TemplateEngine;
import org.drdevelopment.webtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetsServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetsServlet.class);
	private String folder;
	private TemplateEngine templateEngine;
	private List<String> dynamicStaticPages;
	private Boolean hasUsersInSystem = null;

	public AssetsServlet(String folder) {
		super();
		this.folder = folder;
	}

	public AssetsServlet(String folder, List<String> dynamicStaticPages) {
		super();
		this.folder = folder;
		this.dynamicStaticPages = dynamicStaticPages;
	}

	public AssetsServlet(String folder, TemplateEngine templateEngine, List<String> dynamicStaticPages) {
		super();
		this.folder = folder;
		this.templateEngine = templateEngine;
		this.dynamicStaticPages = dynamicStaticPages;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);

		String uri = withoutParameters(request.getRequestURI());
//		LOGGER.info(uri);
		if (!hasUsers()) {
			LOGGER.debug("No users in the system, so, first create a new user.");
			response.sendRedirect("/setup/index.html");
			return;
		}

		String param = getDynamicPage(uri);
		if (param != null) {
			uri = uri.substring(0, uri.length() - param.length()) + "/";
		}
		
		String fileExtension = getFileExtension(uri);
		if (fileExtension == null || fileExtension.isEmpty()) {
			String newUri = "";
			if (uri.endsWith("/") || uri.endsWith("\\")) {
				newUri = uri + "index.html";
			} else {
				newUri = uri + "/index.html";
			}
			
			LOGGER.warn("Could not find extension in uri {} and will be redirected to {}.", uri, newUri);
			uri = newUri;

			fileExtension = getFileExtension(uri);
			if (param == null) {
				response.sendRedirect(request.getContextPath() + uri);
				return;
			}
		}

		String fullUri = getFullUri(folder, uri);
		String contentType = getContentType(fileExtension);
		response.setContentType(contentType + "; charset=utf-8");
		
//		LOGGER.debug("Request uri is {} and converted to {}.", uri, fullUri);

		if (FileExtenstionMediaType.IMAGE.isMediaType(fileExtension)) {
			ImageServlet.getImage(request, response, folder);
		} else if (isTemplateText(fileExtension)) {
			readTemplate(response, fullUri, param);
		} else if (isReadableText(fileExtension)) {
			readText(response, fullUri, param);
		} else if (isBinary(fileExtension)) {
			readBinary(response, fullUri);
		} else {
			LOGGER.error("Could not find resource uri {} which is mapped to file {}.", uri, fullUri);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private String getDynamicPage(String uri) {
		String param = null;
		if (dynamicStaticPages != null) {
			for (String dynamicStaticPage : dynamicStaticPages) {
				if (uri.toLowerCase().startsWith(dynamicStaticPage.toLowerCase()) && !uri.toLowerCase().endsWith("index.html")) {
					param = uri.substring(dynamicStaticPage.length());
				}
			}
		}
		return param;
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

	private void readText(HttpServletResponse response, String uri, String param) {
		// check if there is a template. If so, parse this one first, else try to locate the none-template.
		try {
			String source = readTemplate(response, uri, param); // + ".template");
			if (source != null) {
				response.getWriter().println(source);
			} else {
				if (FileUtil.isResourceExists(uri)) {
					response.getWriter().println(FileUtil.readLinesWithCarriageReturnFromResource(uri));
				} else if (FileUtil.isFileExists(uri)) {
					response.getWriter().println(FileUtil.readFile(uri));
				} else {
					LOGGER.error("Can not find file or resource with uri {}.", uri);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		} catch(IOException e) {
			LOGGER.debug("Could not find resource with uri {}", uri);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private String readTemplate(HttpServletResponse response, String uri, String param) {
		String source = null;
		if (templateEngine != null) {
			source = templateEngine.get(uri, param);
		}
		return source;
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

    private static String withoutParameters(String uri) {
    	if (uri == null || uri.isEmpty()) {
    		return "";
    	} else {
    		String newUri = uri;
    		int posDotComma = newUri.indexOf(';');
    		if (posDotComma > -1) {
    			newUri = newUri.substring(0, posDotComma);
    		}
    		int posQuestionMark = newUri.indexOf('?');
    		if (posQuestionMark > -1) {
    			newUri = newUri.substring(0, posQuestionMark);
    		}
    		int posQuestionHash = newUri.indexOf('#');
    		if (posQuestionHash > -1) {
    			newUri = newUri.substring(0, posQuestionHash);
    		}
    		return newUri.trim();
    	}    			
    }
    
	private String getFileExtension(String uri) {
		if (uri != null && !uri.isEmpty()) {
			int pos = uri.lastIndexOf('.');
			if (pos >= 0) {
				return uri.substring(pos);
			}
		}
		return null;
	}

	private static boolean isTemplateText(String fileExtension) {
		return FileExtenstionMediaType.TEMPLATE.isMediaType(fileExtension);
	}
	
	private static boolean isReadableText(String fileExtension) {
		return FileExtenstionMediaType.JAVASCRIPT.isMediaType(fileExtension) || 
				FileExtenstionMediaType.HTML.isMediaType(fileExtension) ||
				FileExtenstionMediaType.CSS.isMediaType(fileExtension);
	}

	private static boolean isBinary(String fileExtension) {
		return FileExtenstionMediaType.TTF.isMediaType(fileExtension) || FileExtenstionMediaType.WOFF.isMediaType(fileExtension);
	}

	private static String getContentType(String fileExtension) {
		String contentType = null;
		if (FileExtenstionMediaType.JAVASCRIPT.isMediaType(fileExtension)) {
			contentType = "application/javascript";
		} else if (FileExtenstionMediaType.CSS.isMediaType(fileExtension)) {
			contentType = "text/css";
		} else if (FileExtenstionMediaType.HTML.isMediaType(fileExtension)) {
			contentType = "text/html";
		} else if (FileExtenstionMediaType.JSON.isMediaType(fileExtension)) {
			contentType = "application/json";
		} else if (FileExtenstionMediaType.WOFF.isMediaType(fileExtension)) {
			contentType = "application/font-woff2";
		} else if (FileExtenstionMediaType.TTF.isMediaType(fileExtension)) {
			contentType = "application/x-font-ttf";
		} else if (FileExtenstionMediaType.EOT.isMediaType(fileExtension)) {
			contentType = "application/vnd.ms-fontobject";
		} else {
			contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileExtension);
		}

		if (contentType == null || contentType.isEmpty()) {
			LOGGER.error("Content type for file extension {} could not be defined.", fileExtension);
		}
		return contentType;
	}
	
    private boolean hasUsers() {
    	if (hasUsersInSystem == null || !hasUsersInSystem) {
    		hasUsersInSystem = UsersRepository.getUsers().size() > 0;
    	}
    	return hasUsersInSystem;
    }

}