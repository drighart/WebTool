package org.drdevelopment.webtool.plugin.api;

import java.util.List;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface PluginAssetsServlet extends ExtensionPoint {

	String getContextPath();

	String getResourcePath();

	List<String> getDynamicStaticPages();
}
