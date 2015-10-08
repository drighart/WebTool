package org.drdevelopment.webtool.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.ConfigurationItemDao;
import org.drdevelopment.webtool.model.ConfigurationItem;
import org.drdevelopment.webtool.plugin.model.ConfigurationTypeOfInput;

public class ConfigurationItemRepository {

	private ConfigurationItemRepository() {
		// no constructor.
	}
	
	public static List<ConfigurationItem> getConfigurationItemsByPlugin() {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
        List<ConfigurationItem> list = dao.find();
        Collections.sort(list, new Comparator<ConfigurationItem>() {
			@Override
			public int compare(ConfigurationItem o1, ConfigurationItem o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		Services.getServices().getDbi().close(dao);
        return list;
	}

	
	public static List<ConfigurationItem> getConfigurationItems() {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
        List<ConfigurationItem> list = dao.find();
        Collections.sort(list, new Comparator<ConfigurationItem>() {
			@Override
			public int compare(ConfigurationItem o1, ConfigurationItem o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		Services.getServices().getDbi().close(dao);
        return list;
	}

	public static ConfigurationItem findByName(String name) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
        ConfigurationItem item = dao.find(name);
		Services.getServices().getDbi().close(dao);
        return item;
	}

	public static ConfigurationItem findByName(String pluginId, String name) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
        ConfigurationItem item = dao.find(pluginId, name);
		Services.getServices().getDbi().close(dao);
        return item;
	}

	public static void update(String name, String value) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
		dao.save(name, value);
		Services.getServices().getDbi().close(dao);
	}

	public static void update(String pluginId, ConfigurationTypeOfInput typeOfInput, String name, String value) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
		dao.save(pluginId, typeOfInput.name(), name, value);
		Services.getServices().getDbi().close(dao);
	}

	public static void insert(String pluginId, ConfigurationTypeOfInput typeOfInput, String name, String value) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
		dao.insert(pluginId, typeOfInput.name(), name, value);
		Services.getServices().getDbi().close(dao);
	}

}
