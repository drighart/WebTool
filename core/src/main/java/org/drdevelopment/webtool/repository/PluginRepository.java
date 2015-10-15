package org.drdevelopment.webtool.repository;

import java.util.List;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.WebServer;
import org.drdevelopment.webtool.dao.PluginDataVersionDao;
import org.drdevelopment.webtool.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginRepository.class);

	private PluginRepository() {
		// no constructor.
	}
		
	public static void setAllRunning(boolean allRunningFlag) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        dao.setRunning(allRunningFlag);
        Services.getServices().getDbi().close(dao);
	}

	public static void setRunning(String pluginId, boolean runningFlag) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        int result = dao.setRunning(pluginId, runningFlag);
        if (result != 1) {
        	LOGGER.warn("The running flag for plugin {} could not be set!", pluginId);
        }
        Services.getServices().getDbi().close(dao);
	}

	public static List<Plugin> find() {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        List<Plugin> list = dao.findPlugings();
        Services.getServices().getDbi().close(dao);
        return list;
	}

	public static Plugin find(String pluginId) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        Plugin plugin = dao.find(pluginId);
        Services.getServices().getDbi().close(dao);
        return plugin;
	}
	
	public static void insert(String pluginId, int version) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
		dao.insert(pluginId, version);
        Services.getServices().getDbi().close(dao);
	}

}
