package org.drdevelopment.webtool.plugin;

import java.io.File;
import java.util.List;

import org.drdevelopment.webtool.plugin.api.PluginAssetsServlet;
import org.drdevelopment.webtool.plugin.util.FileUtil;

import ro.fortsoft.pf4j.Extension;

@Extension
public class DashboardLoginServletExtension implements PluginAssetsServlet {

	@Override
	public String getContextPath() {
		return "/login/*";
	}

	@Override
	public String getResourcePath() {
		return FileUtil.getCurrentFolder() + File.separator + "plugins" + File.separator + DashboardPlugin.FOLDER_NAME + 
				File.separator +"classes" + File.separator + "web";
	}

	@Override
	public List<String> getDynamicStaticPages() {
		return null;
	}

}
