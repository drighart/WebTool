package org.drdevelopment.webtool.plugin;

import javax.sql.DataSource;

import org.drdevelopment.webtool.plugin.api.model.PageItem;
import org.drdevelopment.webtool.plugin.api.model.PagesItem;

public class PluginServices implements CoreServices {
	private DataSource dataSource;
	private CoreServices coreServices;
	private static PluginServices services;
	
	private PluginServices() {
	}
	
	public static PluginServices getServices() {
		if (services == null) {
			services = new PluginServices();
		}
		return services;
	}

	public CoreServices getCoreServices() {
		return coreServices;
	}

	public void setCoreServices(CoreServices coreServices) {
		this.coreServices = coreServices;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = coreServices.getDataSource();
		}
		return dataSource;
	}

	@Override
	public String getConfigurationItem(String name) {
		return coreServices.getConfigurationItem(name);
	}

	@Override
	public PagesItem getPages() {
		return coreServices.getPages();
	}
	
}
