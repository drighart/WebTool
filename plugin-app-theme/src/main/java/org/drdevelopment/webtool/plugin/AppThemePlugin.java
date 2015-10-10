package org.drdevelopment.webtool.plugin;

import java.io.File;

import org.drdevelopment.webtool.plugin.util.FileUtil;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public class AppThemePlugin extends Plugin {
    public static final String FOLDER_NAME = "drdevelopment-app-theme-plugin-0.2-SNAPSHOT";
    public static final String RESOURCE_LOCATION = FileUtil.getCurrentFolder() + File.separator + "plugins" + File.separator + 
    		AppThemePlugin.FOLDER_NAME + File.separator + "classes" + File.separator + "web";
    
	public AppThemePlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

}
