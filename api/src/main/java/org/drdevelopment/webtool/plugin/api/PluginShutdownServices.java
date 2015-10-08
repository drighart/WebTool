package org.drdevelopment.webtool.plugin.api;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface PluginShutdownServices extends ExtensionPoint {

	void shutdown();

}
