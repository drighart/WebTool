package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.MenuItemDao;
import org.drdevelopment.webtool.model.MenuItem;

public class MenuItemRepository {

	private MenuItemRepository() {
		// no constructor.
	}
	
	public static MenuItem find(Integer position) {
		MenuItemDao dao = Services.getServices().getDbi().open(MenuItemDao.class);
		MenuItem menuItem = dao.find(position);
        Services.getServices().getDbi().close(dao);
        return menuItem;
	}
	
	public static void update(Integer position, String name, Boolean onCurrentPage, Integer pageId) {
		MenuItemDao dao = Services.getServices().getDbi().open(MenuItemDao.class);
		dao.update(position, name, onCurrentPage, pageId);
        Services.getServices().getDbi().close(dao);
	}

	public static void remove(Integer position) {
		MenuItemDao dao = Services.getServices().getDbi().open(MenuItemDao.class);
		dao.remove(position);
        Services.getServices().getDbi().close(dao);
	}

}
