package org.drdevelopment.webtool.plugin.api;

import ro.fortsoft.pf4j.ExtensionPoint;


public interface PluginHealthService extends ExtensionPoint {

	/**
	 * The health service checks every interval if something is wrong with the system. A rest service is available to check
	 * if something is wrong. If an exception occurs during a health check, this exception is returned in the rest service.
	 * 
	 * @return An exception when something is wrong with the plugin for which this service is implemented. Return null if
	 * everything is ok.
	 */
	Exception getStatus();
		
}
