package org.drdevelopment.webtool.repository;

import java.util.List;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.H2DbDao;
import org.drdevelopment.webtool.model.H2DbSession;

public class H2DbRepository {

	private H2DbRepository() {
		// no constructor.
	}
	
	public static List<H2DbSession> getSessions() {
		H2DbDao dao = Services.getServices().getDbi().open(H2DbDao.class);
		List<H2DbSession> session = dao.getSessions();
        Services.getServices().getDbi().close(dao);
        return session;
	}
	
}
