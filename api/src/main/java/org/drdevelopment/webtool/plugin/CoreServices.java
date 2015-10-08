package org.drdevelopment.webtool.plugin;

import javax.sql.DataSource;

import org.drdevelopment.webtool.plugin.api.model.PagesItem;

public interface CoreServices {

	DataSource getDataSource();

	String getConfigurationItem(String name);

	PagesItem getPages();

}
