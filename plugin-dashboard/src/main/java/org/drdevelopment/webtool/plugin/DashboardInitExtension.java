package org.drdevelopment.webtool.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.drdevelopment.webtool.api.TemplateProcessor;
import org.drdevelopment.webtool.plugin.api.PluginInitServices;
import org.drdevelopment.webtool.plugin.api.model.ConfigurationItem;
import org.drdevelopment.webtool.plugin.api.model.Image;
import org.drdevelopment.webtool.plugin.model.ConfigurationItemImpl;
import org.drdevelopment.webtool.plugin.model.ConfigurationTypeOfInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Version;

@Extension
public class DashboardInitExtension implements PluginInitServices {
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardInitExtension.class);

	@Override
	public void init(String pluginId, String pluginDescription, String pluginClass, Version pluginVersion,
			DataSource dataSource) {
		LOGGER.info("INIT PLUGIN: {} - {} version {}", pluginId, pluginDescription, pluginVersion);
	}

	@Override
	public List<TemplateProcessor> getProcessors() {
		return null;
	}

	@Override
	public List<ConfigurationItem> getConfigurationItems() {
		List<ConfigurationItem> configurationItems = new ArrayList<>();
//		configurationItems.add(new ConfigurationItemImpl("LINK_COLOR", "#000000", ConfigurationTypeOfInput.COLOR));
		
		return configurationItems;
	}

	@Override
	public Set<Image> getResourceImagesToCopy() {
		return null;
	}

}
