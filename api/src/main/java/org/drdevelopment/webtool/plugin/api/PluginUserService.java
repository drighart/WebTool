package org.drdevelopment.webtool.plugin.api;

import java.util.List;

import org.drdevelopment.webtool.exception.PluginException;
import org.drdevelopment.webtool.plugin.authentication.User;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface PluginUserService extends ExtensionPoint {

	void addUser(String loginUsername, String name, String password, String emailAddress, String fullName) throws PluginException;
	
	/**
	 * @param name the name of the user.
	 * @param password the password of the user.
	 * @return null when the user is not authenticated.
	 */
	User authenticateUser(String name, String password)  throws PluginException;
	
	User getUserDetails(String loginUsername, String name) throws PluginException;
	
	User login(String name, String password) throws PluginException;

	void logout(String loginUsername);
	
	boolean isUserInRole(String loginUsername, String role) throws PluginException;
	
	void addRole(String loginUsername, String role) throws PluginException;

	void removeRole(String loginUsername, String role) throws PluginException;

	List<User> getUsers();
}
