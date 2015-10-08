package org.drdevelopment.webtool.plugin;

import org.drdevelopment.webtool.exception.TechnicalException;
import org.drdevelopment.webtool.plugin.api.PluginDataupdate;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class UsersDataupdateExtension implements PluginDataupdate {
	private static final String PLUGIN_FOLDER = "drdevelopment-users-plugin-1.0-SNAPSHOT";
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersDataupdateExtension.class);
	private static final int FINAL_DATA_UPDATE_VERSION = 1;
	
	@Override
	public int getFinalDataupdateVersion() {
		return FINAL_DATA_UPDATE_VERSION;
	}

	@Override
	public String getDataUpdate(int currentDataupdateVersion) {
		String filename = FileUtil.getPluginClassesFolder(PLUGIN_FOLDER) + "/dataupdate/dataupdate" + currentDataupdateVersion + ".sql";
		if (FileUtil.isFileExists(filename)) {
			try {
				String sql = FileUtil.readFile(filename);
				return sql;
			} catch(Exception e) {
				// This technical exception will shutdown the application.
				throw new TechnicalException("Could not read dataupdate file with name {}", filename);
			}
		} else {
			LOGGER.warn("No dataupdate found for version {} and file {}.", currentDataupdateVersion, filename);
			return "";
		}
	}


}
