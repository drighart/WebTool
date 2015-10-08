package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.HealthDao;

public class HealthRepository {

	private HealthRepository() {
		// no constructor.
	}
	
	public static boolean check() {
		HealthDao dao = Services.getServices().getDbi().open(HealthDao.class);
		Integer status = dao.check();
		Services.getServices().getDbi().close(dao);
		return status == 1;
	}

}
