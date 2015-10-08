package org.drdevelopment.webtool.plugin;

import ro.fortsoft.pf4j.PluginDescriptor;
import ro.fortsoft.pf4j.Version;

public class LionExtensionDescriptor {

    private int ordinal;
    private Class<?> extensionClass;
    private String pluginId;
    private Version version;
    private String description;
    private String plugInClass;

    public Class<?> getExtensionClass() {
        return extensionClass;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setExtensionClass(Class<?> extensionClass) {
        this.extensionClass = extensionClass;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlugInClass() {
		return plugInClass;
	}

	public void setPlugInClass(String plugInClass) {
		this.plugInClass = plugInClass;
	}

}
