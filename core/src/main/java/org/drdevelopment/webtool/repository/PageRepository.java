package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.PageDao;
import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.rest.PageRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(PageRepository.class);

	private PageRepository() {
		// no constructor.
	}
	
	public static Page find(Integer id) {
		PageDao dao = Services.getServices().getDbi().open(PageDao.class);
		Page page = dao.find(id);
        Services.getServices().getDbi().close(dao);
        return page;
	}

	public static Page findByName(String name) {
		PageDao dao = Services.getServices().getDbi().open(PageDao.class);
		Page page = dao.find(name);
        Services.getServices().getDbi().close(dao);
        return page;
	}

	public static void update(Integer id, String title) {
		PageDao dao = Services.getServices().getDbi().open(PageDao.class);
		dao.update(id, title);
        Services.getServices().getDbi().close(dao);
	}

	public static void insert(String name, String title) {
		String alphaAndDigits = name.replaceAll("[^a-zA-Z0-9]+", "");
		if (alphaAndDigits == null) {
			LOGGER.warn("Could not create new page, because of an invalid name {}.", name);
		} else {
			PageDao dao = Services.getServices().getDbi().open(PageDao.class);
			dao.insert(alphaAndDigits, title);
			Services.getServices().getDbi().close(dao);
		}
	}

	public static void remove(Integer id) {
		PageDao dao = Services.getServices().getDbi().open(PageDao.class);
		dao.remove(id);
        Services.getServices().getDbi().close(dao);
	}

}
