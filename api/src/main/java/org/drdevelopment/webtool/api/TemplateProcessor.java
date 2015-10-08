package org.drdevelopment.webtool.api;


public interface TemplateProcessor {

	String getTag();
	
	String execute(String name, String uri, String param);
		
}
