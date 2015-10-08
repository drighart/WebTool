package org.drdevelopment.webtool.plugin;

import java.util.ArrayList;
import java.util.List;

import ro.fortsoft.pf4j.DefaultExtensionFactory;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.ExtensionFinder;
import ro.fortsoft.pf4j.ExtensionWrapper;

public class LionPluginManager extends DefaultPluginManager {
	private LionExtensionFinder extensionFinder;
	
    /**
     * Add the possibility to override the ExtensionFinder.
     */
    protected ExtensionFinder createExtensionFinder() {
    	LionExtensionFinder extensionFinder = new LionExtensionFinder(this, new DefaultExtensionFactory());
    	this.extensionFinder = extensionFinder;
        addPluginStateListener(extensionFinder);

        return extensionFinder;
    }

	public <T> List<T> getExtensionsByPlugin(Class<T> type, String pluginId) {
		List<LionExtensionWrapper<T>> extensionsWrapper = extensionFinder.find(type, pluginId);
		List<T> extensions = new ArrayList<>(extensionsWrapper.size());
		for (LionExtensionWrapper<T> extensionWrapper : extensionsWrapper) {
			extensions.add(extensionWrapper.getExtension());
		}

		return extensions;
	}

	public <T> List<T> getExtensionsByPlugin(String pluginId) {
		List<LionExtensionWrapper<T>> extensionsWrapper = extensionFinder.find(pluginId);
		List<T> extensions = new ArrayList<>(extensionsWrapper.size());
		for (LionExtensionWrapper<T> extensionWrapper : extensionsWrapper) {
			extensions.add(extensionWrapper.getExtension());
		}

		return extensions;
	}

	public <T> List<T> getExtensionsByType(Class<T> type) {
		List<LionExtensionWrapper<T>> extensionsWrapper = extensionFinder.findByType(type);
		List<T> extensions = new ArrayList<>(extensionsWrapper.size());
		for (LionExtensionWrapper<T> extensionWrapper : extensionsWrapper) {
			extensions.add(extensionWrapper.getExtension());
		}

		return extensions;
	}

	public <T> List<LionExtensionWrapper<T>> getExtensionsAndDescriptorsByType(Class<T> type) {
		return extensionFinder.findByType(type);
	}

}
