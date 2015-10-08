package org.drdevelopment.webtool.plugin.model;

import org.drdevelopment.webtool.plugin.api.model.ConfigurationItem;

public class ConfigurationItemImpl implements ConfigurationItem {
	private String name;
	private String defaultValue;
	private ConfigurationTypeOfInput kindOfInput;

	public ConfigurationItemImpl(String name, String defaultValue, ConfigurationTypeOfInput kindOfInput) {
		super();
		this.name = name;
		this.defaultValue = defaultValue;
		this.kindOfInput = kindOfInput;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public ConfigurationTypeOfInput getKindOfInput() {
		return kindOfInput;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigurationItemImpl other = (ConfigurationItemImpl) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfigurationItemImpl [name=" + name + ", defaultValue=" + defaultValue + "]";
	}

}
