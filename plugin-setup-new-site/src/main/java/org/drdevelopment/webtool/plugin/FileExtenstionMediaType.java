package org.drdevelopment.webtool.plugin;

import java.util.Arrays;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

public enum FileExtenstionMediaType {
	IMAGE(".jpg", ".jpeg", ".gif", ".png", ".ico"),
	JAVASCRIPT(".js", ".javascript", ".map"),
	JSON(".json"),
	CSS(".css"),
	WOFF(".woff", ".woff2"),
	TTF(".ttf"),
	EOT(".eot"),
	HTML(".html"),
	TEMPLATE(".template");

	private List<String> fileExtensions;

	private FileExtenstionMediaType(String... fileExtension) {
		this.fileExtensions = Arrays.asList(fileExtension);
	}
	
	public boolean isMediaType(String fileExtension) {
		if (fileExtension == null) {
			return false;
		}
		return fileExtensions.contains(fileExtension.toLowerCase());
	}

	public static String getFileExtension(String uri) {
		if (uri != null && !uri.isEmpty()) {
			int pos = uri.lastIndexOf('.');
			if (pos >= 0) {
				return uri.substring(pos).trim();
			}
		}
		return null;
	}

    public static String getContentType(String fileExtension) {
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
