package org.drdevelopment.webtool.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.drdevelopment.webtool.plugin.api.PluginAssetsServlet;
import org.drdevelopment.webtool.plugin.util.FileUtil;

import ro.fortsoft.pf4j.Extension;

@Extension
public class DashboardServletExtension implements PluginAssetsServlet {

	@Override
	public String getContextPath() {
		return "/dashboard/*";
	}

	@Override
	public String getResourcePath() {
		return FileUtil.getCurrentFolder() + File.separator + "plugins" + File.separator + DashboardPlugin.FOLDER_NAME + 
				File.separator +"classes" + File.separator + "web";
	}

	@Override
	public List<String> getDynamicStaticPages() {
		List<String> staticPages = new ArrayList<>();
		return staticPages;
	}

}
