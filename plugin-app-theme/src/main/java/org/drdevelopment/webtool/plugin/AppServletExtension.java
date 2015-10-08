package org.drdevelopment.webtool.plugin;

import java.util.ArrayList;
import java.util.List;

import org.drdevelopment.webtool.plugin.api.PluginAssetsServlet;

import ro.fortsoft.pf4j.Extension;

@Extension
public class AppServletExtension implements PluginAssetsServlet {

	@Override
	public String getContextPath() {
		return "/app/*";
	}

	@Override
	public String getResourcePath() {
		return AppThemePlugin.RESOURCE_LOCATION;
	}

	@Override
	public List<String> getDynamicStaticPages() {
		List<String> staticPages = new ArrayList<>();
		staticPages.add("/app/page");
		return staticPages;
	}

}
