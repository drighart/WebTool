package org.drdevelopment.webtool.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.MenuItemsDao;
import org.drdevelopment.webtool.model.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MenuItemsRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemsRepository.class);

	private MenuItemsRepository() {
		// no constructor.
	}
	
	public static List<MenuItem> getMenuItems() {
		MenuItemsDao dao = Services.getServices().getDbi().open(MenuItemsDao.class);
        List<MenuItem> list = dao.find();
        Collections.sort(list, new Comparator<MenuItem>() {
			@Override
			public int compare(MenuItem o1, MenuItem o2) {
				return o1.getPosition().compareTo(o2.getPosition());
			}
		});
        Services.getServices().getDbi().close(dao);
        return list;
	}
	
	public static void insert(String name) {
		String alphaAndDigits = name.replaceAll("[^a-zA-Z0-9]+", "");
		if (alphaAndDigits == null) {
			LOGGER.warn("Could not create new page, because of an invalid name {}.", name);
		} else {
			MenuItemsDao dao = Services.getServices().getDbi().open(MenuItemsDao.class);
			dao.newMenuItem(name);
			Services.getServices().getDbi().close(dao);
		}
	}

	private static int getIndex(Integer position, List<MenuItem> menuitems) {
		int index = -1;
		for (MenuItem menuitem : menuitems) {
			index++;
			if (position.equals(menuitem.getPosition())) {
				return index;
			}
		}
		return -1;
	}
	
	public static void up(Integer position) {
		MenuItemsDao dao = Services.getServices().getDbi().open(MenuItemsDao.class);
		List<MenuItem> menuitems = getMenuItems();
		int index = getIndex(position, menuitems) - 1;
		int currentIndex = getIndex(position, menuitems);
		if (index >= 0 && currentIndex >= 0) {
			MenuItem menuItem1 = menuitems.get(index);
			MenuItem menuItem2 = menuitems.get(currentIndex);
			dao.updatePosition(menuItem1.getPosition(), -1);
			dao.updatePosition(menuItem2.getPosition(), menuItem1.getPosition());
			dao.updatePosition(-1, menuItem2.getPosition());
		} else {
			LOGGER.warn("Changing menu item up with position {} not possible.", position);
		}
	}

	public static void down(Integer position) {
		MenuItemsDao dao = Services.getServices().getDbi().open(MenuItemsDao.class);
		List<MenuItem> menuitems = getMenuItems();
		int index = getIndex(position, menuitems) + 1;
		int currentIndex = getIndex(position, menuitems);
		if (index >= 0 && currentIndex >= 0 && index < menuitems.size()) {
			MenuItem menuItem1 = menuitems.get(index);
			MenuItem menuItem2 = menuitems.get(currentIndex);
			dao.updatePosition(menuItem1.getPosition(), -1);
			dao.updatePosition(menuItem2.getPosition(), menuItem1.getPosition());
			dao.updatePosition(-1, menuItem2.getPosition());
		} else {
			LOGGER.warn("Changing menu item down with position {} not possible.", position);
		}
	}

	public static void remove(String name) {
		MenuItemsDao dao = Services.getServices().getDbi().open(MenuItemsDao.class);
		dao.remove(name);
        Services.getServices().getDbi().close(dao);
	}

}
