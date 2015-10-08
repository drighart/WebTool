package org.drdevelopment.webtool.plugin.authentication;

import java.util.List;

public interface User {
	String getName();
	
	List<String> getRoles();

	List<String> getGroups();

	String getEmailAddress();
	
	String getFullName();
	
}
