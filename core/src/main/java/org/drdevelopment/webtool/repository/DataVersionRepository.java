package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.DataVersionDao;
import org.drdevelopment.webtool.dao.PluginDataVersionDao;
import org.drdevelopment.webtool.model.DataVersion;

public class DataVersionRepository {

	private DataVersionRepository() {
		// no constructor.
	}
	
	public static Integer getDataVersion() {
        DataVersionDao dao = Services.getServices().getDbi().open(DataVersionDao.class);
        DataVersion dataVersion = dao.findDataUpdateVersion();
        Services.getServices().getDbi().close(dao);
        return (dataVersion == null ? null : dataVersion.getDataVersion());
	}
	
	public static void insert() {
        DataVersionDao dao = Services.getServices().getDbi().open(DataVersionDao.class);
        dao.insert(0);
        Services.getServices().getDbi().close(dao);
	}

	public static void update(Integer version) {
        DataVersionDao dao = Services.getServices().getDbi().open(DataVersionDao.class);
        dao.update(version);
        Services.getServices().getDbi().close(dao);
	}

	public static Integer getPluginDataVersion(String pluginId) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        DataVersion dataVersion = dao.findDataUpdateVersion(pluginId);
        Services.getServices().getDbi().close(dao);
        return (dataVersion == null ? null : dataVersion.getDataVersion());
	}
	
	public static void pluginInsertNew(String pluginId) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        dao.insert(pluginId, 0);
        Services.getServices().getDbi().close(dao);
	}

	public static void pluginUpdate(String pluginId, Integer version) {
		PluginDataVersionDao dao = Services.getServices().getDbi().open(PluginDataVersionDao.class);
        dao.update(pluginId, version);
        Services.getServices().getDbi().close(dao);
	}

}
