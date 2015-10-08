package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.UserDao;
import org.drdevelopment.webtool.model.User;

public class UserRepository {

	private UserRepository() {
		// no constructor.
	}
	
	public static User find(String emailAddress) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		User user = dao.findUser(emailAddress);
        Services.getServices().getDbi().close(dao);
        return user;
	}
	
	public static void update(String emailAddress, String roles, String displayName) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		dao.update(emailAddress, roles, displayName);
        Services.getServices().getDbi().close(dao);
	}

	public static void insert(String emailAddress, String roles, String displayName, String language) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		dao.insert(emailAddress, roles, displayName, language);
        Services.getServices().getDbi().close(dao);
	}

	public static void remove(String emailAddress) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		dao.remove(emailAddress);
        Services.getServices().getDbi().close(dao);
	}

	public static void changeCredentials(String emailAddress, String credentials) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		dao.changeCredentials(emailAddress, credentials);
        Services.getServices().getDbi().close(dao);
	}

	public static void activate(String emailAddress) {
		UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		dao.activate(emailAddress);
        Services.getServices().getDbi().close(dao);
	}

}
