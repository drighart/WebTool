package org.drdevelopment.webtool.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.UsersDao;
import org.drdevelopment.webtool.model.User;

public class UsersRepository {

	private UsersRepository() {
		// no constructor.
	}
	
	public static List<User> getUsers() {
		UsersDao dao = Services.getServices().getDbi().open(UsersDao.class);
        List<User> list = dao.find();
        Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getEmailAddress().compareTo(o2.getEmailAddress());
			}
		});
        Services.getServices().getDbi().close(dao);
        return list;
	}
	
}
