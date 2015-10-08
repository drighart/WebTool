package org.drdevelopment.webtool.plugin.api;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.drdevelopment.webtool.api.TemplateProcessor;
import org.drdevelopment.webtool.plugin.api.model.ConfigurationItem;
import org.drdevelopment.webtool.plugin.api.model.Image;

import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.Version;

public interface PluginInitServices extends ExtensionPoint {

	void init(String pluginId, String pluginDescription, String pluginClass, Version pluginVersion, DataSource dataSource);

	Set<Image> getResourceImagesToCopy();
	
	List<TemplateProcessor> getProcessors();
	
	List<ConfigurationItem> getConfigurationItems();
}
