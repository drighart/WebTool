package org.drdevelopment.webtool.plugin.api.model;

import org.drdevelopment.webtool.plugin.model.ConfigurationTypeOfInput;

public interface ConfigurationItem {

	String getName();

	String getDefaultValue();
	
	ConfigurationTypeOfInput getKindOfInput();
}
