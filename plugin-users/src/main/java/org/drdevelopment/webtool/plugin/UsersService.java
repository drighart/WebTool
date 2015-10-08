package org.drdevelopment.webtool.plugin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.exception.PluginException;
import org.drdevelopment.webtool.plugin.PluginServices;
import org.drdevelopment.webtool.plugin.api.PluginUserService;
import org.drdevelopment.webtool.plugin.authentication.User;
import org.drdevelopment.webtool.plugin.model.Role;
import org.drdevelopment.webtool.plugin.model.dao.UserDao;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class UsersService implements PluginUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);
    
    @Override
	public void addUser(String loginUsername, String name, String password, String emailAddress, String fullName) throws PluginException {
		checkInRole(loginUsername, Role.ADMIN);
		String hashPassword = hashPassword(password);
		User user = findUser(name.toLowerCase());
		if (user == null) {
			LOGGER.debug("Created new user with name '{}'.", name);
			UserDao dao = getUserDao();
			dao.createNewUser(name, hashPassword, fullName, emailAddress);
			dao.close();
		} else {
			throw new PluginException("User {} already exists.", name);
		}
	}

	private User findUser(String name) {
		UserDao dao = getUserDao();
		Object user = dao.findUser(name.toLowerCase());
		dao.close();
		if (user == null) {
			return null;
		} else {
			return (User) user;
		}
	}
	
	private UserDao getUserDao() {
		DBI dbi = new DBI(PluginServices.getServices().getDataSource());
		UserDao dao = dbi.open(UserDao.class);
		return dao;
	}
	
	private String hashPassword(String password) throws PluginException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] mdbytes = md.digest();

	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new PluginException(e);
		}
	}
	
	@Override
	public User authenticateUser(String name, String password) throws PluginException {
		UserDao dao = getUserDao();
		Object user = dao.checkPassword(name.toLowerCase(), hashPassword(password));
		dao.close();
		if (user == null) {
			return null;
		} else {
			return (User) user;
		}
	}

	private User checkInRole(String loginUsername, Role role) throws PluginException {
		if (loginUsername == null || loginUsername.isEmpty()) {
			throw new IllegalArgumentException("Login user name is not filled.");
		} else {
			User loginUser = getUserDetails(loginUsername, loginUsername);
			if (loginUser == null) {
				throw new PluginException("User {} does not exist.", loginUsername);
			} else if (!isUserInRole(loginUsername, role.name())) {
				throw new PluginException("User {} has not role {}.", loginUser.getName(), role.name());
			}
			return loginUser;
		}
	}
	
	@Override
	public User getUserDetails(String loginUsername, String name) throws PluginException {
		User foundUser = null;
		if (loginUsername != null && name != null && !loginUsername.equalsIgnoreCase(name)) {
			checkInRole(loginUsername, Role.ADMIN);
		} else if (name == null) {
			return null;
		} else {
			UserDao dao = getUserDao();
			Object user = dao.findUser(name.toLowerCase());
			dao.close();
			if (user != null) {
				foundUser = (User) user;
			}
		}
		return foundUser;
	}

	@Override
	public User login(String name, String password) throws PluginException {
		String hashPassword = hashPassword(password);
		UserDao dao = getUserDao();
		Object user = dao.checkPassword(name.toLowerCase(), hashPassword);
		dao.close();
		if (user == null) {
			return null;
		} else {
			// TODO set session or something.
			return (User) user;
		}
	}

	@Override
	public void logout(String loginUsername) {
	}

	@Override
	public boolean isUserInRole(String loginUsername, String role) throws PluginException {
		User loginUser = getUserDetails(loginUsername, loginUsername);
		return (loginUser != null && loginUser.getRoles().contains(role.toUpperCase()));
	}

	@Override
	public void addRole(String loginUsername, String role) throws PluginException {
		User loginUser = checkInRole(loginUsername, Role.ADMIN);
		UserDao dao = getUserDao();
		if (role == null || role.isEmpty()) {
			throw new PluginException("Role is null or empty.");
		} else if (loginUser.getRoles().contains(role.toUpperCase())) {
			throw new PluginException("User with name '{}' has already role {}.", loginUser.getName(), role);
		} else {
			loginUser.getRoles().add(role.toUpperCase());
			dao.updateRoles(loginUser.getName(), StringUtils.join(loginUser.getRoles(), ", "));
		}
		dao.close();
	}

	@Override
	public void removeRole(String loginUsername, String role) throws PluginException {
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		UserDao dao = getUserDao();
		List<User> users = dao.findUsers();
		if (users == null) {
			users = new ArrayList<>();
		}
		dao.close();
		return users;
	}

}
