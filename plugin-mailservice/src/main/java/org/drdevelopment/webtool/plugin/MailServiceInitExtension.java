package org.drdevelopment.webtool.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

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
public class MailServiceInitExtension implements PluginInitServices {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceInitExtension.class);
	private static final String SMTP_DEBUG = "SMTP_DEBUG";
	private static final String SMTP_HOST = "SMTP_HOST";
	private static final String SMTP_PORT = "SMTP_PORT";
	private static final String SMTP_USERNAME = "SMTP_USERNAME";
	private static final String SMTP_PASSWORD = "SMTP_PASSWORD";
	
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
		
		Properties properties = readPropertiesFromResource("/configuration/default.properties");
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			configurationItems.add(new ConfigurationItemImpl(key, value, ConfigurationTypeOfInput.TEXT));
		}
		
		return configurationItems;
	}

    private static Properties readPropertiesFromResource(String configFilename) {
      Properties properties = new Properties();
      try ( InputStream input = MailServiceInitExtension.class.getResourceAsStream(configFilename);
      	) {
          properties.load(input);
      } catch (IOException e) {
      	LOGGER.error(e.getMessage(), e);
		}
      return properties;
    }

	@Override
	public Set<Image> getResourceImagesToCopy() {
		return null;
	}

}
