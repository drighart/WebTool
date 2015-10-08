package org.drdevelopment.webtool.plugin.api;

import ro.fortsoft.pf4j.ExtensionPoint;


public interface PluginDataupdate extends ExtensionPoint {

	/**
	 * Gets the final data update version of which the defined plugin uses. Every data update is increased by one and is
	 * retrieved by the getDataUpdate method.
	 * @return final data update version of the plugin.
	 */
	int getFinalDataupdateVersion();
	
	/**
	 * @param currentDataupdateVersion current data update version to retrieve the SQL for.
	 * @return the SQL which will be executed on the database.
	 */
	String getDataUpdate(int currentDataupdateVersion);
		
}
