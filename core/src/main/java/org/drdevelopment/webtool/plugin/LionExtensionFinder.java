package org.drdevelopment.webtool.plugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.DefaultExtensionFinder;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionDescriptor;
import ro.fortsoft.pf4j.ExtensionFactory;
import ro.fortsoft.pf4j.ExtensionFinder;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.ExtensionWrapper;
import ro.fortsoft.pf4j.ExtensionsIndexer;
import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginState;
import ro.fortsoft.pf4j.PluginStateEvent;
import ro.fortsoft.pf4j.PluginStateListener;
import ro.fortsoft.pf4j.PluginWrapper;

public class LionExtensionFinder implements ExtensionFinder, PluginStateListener {
	private static final Logger log = LoggerFactory.getLogger(LionExtensionFinder.class);

    private PluginManager pluginManager;
	private ExtensionFactory extensionFactory;
    private volatile Map<String, Set<String>> entries; // cache by pluginId

	public LionExtensionFinder(PluginManager pluginManager, ExtensionFactory extensionFactory) {
        this.pluginManager = pluginManager;
		this.extensionFactory = extensionFactory;
	}

	public <T> List<LionExtensionWrapper<T>> find(Class<T> type, String pluginIdLookup) {
        log.debug("Checking extension point '{}'", type.getName());
        if (!isExtensionPoint(type)) {
            log.warn("'{}' is not an extension point", type.getName());

            return Collections.emptyList(); // or return null ?!
        }

		log.debug("Finding extensions for extension point '{}'", type.getName());
        readIndexFiles();

        List<LionExtensionWrapper<T>> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : entries.entrySet()) {
            String pluginId = entry.getKey();

            PluginWrapper pluginWrapper = null;
            if (pluginId != null) {
                pluginWrapper = pluginManager.getPlugin(pluginId);
                if (PluginState.STARTED != pluginWrapper.getPluginState() || !pluginIdLookup.equalsIgnoreCase(pluginId)) {
                    continue;
                }
            }

            Set<String> extensionClassNames = entry.getValue();

            for (String className : extensionClassNames) {
                try {
                    ClassLoader classLoader;
                    if (pluginId != null) {
                        classLoader = pluginManager.getPluginClassLoader(pluginId);
                    } else {
                        classLoader = getClass().getClassLoader();
                    }
                    log.debug("Loading class '{}' using class loader '{}'", className, classLoader);
                    Class<?> extensionClass = classLoader.loadClass(className);

                    log.debug("Checking extension type '{}'", className);
                    if (type.isAssignableFrom(extensionClass) && extensionClass.isAnnotationPresent(Extension.class)) {
                        Extension extension = extensionClass.getAnnotation(Extension.class);
                        LionExtensionDescriptor descriptor = new LionExtensionDescriptor();
                        descriptor.setOrdinal(extension.ordinal());
                        descriptor.setExtensionClass(extensionClass);
                        descriptor.setPluginId(pluginId);
                        if (pluginWrapper != null) {
                        	descriptor.setVersion(pluginWrapper.getDescriptor().getVersion());
                        	descriptor.setDescription(pluginWrapper.getDescriptor().getPluginDescription());
                        	descriptor.setPlugInClass(pluginWrapper.getDescriptor().getPluginClass());
                        }

                        LionExtensionWrapper<T> extensionWrapper = new LionExtensionWrapper<>(descriptor);
                        extensionWrapper.setExtensionFactory(extensionFactory);
                        result.add(extensionWrapper);
                        log.debug("Added extension '{}' with ordinal {}", className, extension.ordinal());
                    } else {
                        log.debug("'{}' is not an extension for extension point '{}'", className, type.getName());
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        if (entries.isEmpty()) {
        	log.debug("No extensions found for extension point '{}'", type.getName());
        } else {
        	log.debug("Found {} extensions for extension point '{}'", entries.size(), type.getName());
        }

        // sort by "ordinal" property
//        Collections.sort(result);

		return result;
	}

	public <T> List<LionExtensionWrapper<T>> find(String pluginIdLookup) {
        readIndexFiles();

        List<LionExtensionWrapper<T>> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : entries.entrySet()) {
            String pluginId = entry.getKey();

            PluginWrapper pluginWrapper = null;
            if (pluginId != null) {
                pluginWrapper = pluginManager.getPlugin(pluginId);
                if (PluginState.STARTED != pluginWrapper.getPluginState() && !pluginIdLookup.equalsIgnoreCase(pluginId)) {
                    continue;
                }
            }

            Set<String> extensionClassNames = entry.getValue();

            for (String className : extensionClassNames) {
                try {
                    ClassLoader classLoader;
                    if (pluginId != null) {
                        classLoader = pluginManager.getPluginClassLoader(pluginId);
                    } else {
                        classLoader = getClass().getClassLoader();
                    }
                    log.debug("Loading class '{}' using class loader '{}'", className, classLoader);
                    Class<?> extensionClass = classLoader.loadClass(className);

                    log.debug("Checking extension type '{}'", className);
//                    if (type.isAssignableFrom(extensionClass) && extensionClass.isAnnotationPresent(Extension.class)) {
                    if (extensionClass.isAnnotationPresent(Extension.class)) {
                        Extension extension = extensionClass.getAnnotation(Extension.class);
                        LionExtensionDescriptor descriptor = new LionExtensionDescriptor();
                        descriptor.setOrdinal(extension.ordinal());
                        descriptor.setExtensionClass(extensionClass);
                        descriptor.setPluginId(pluginId);
                        if (pluginWrapper != null) {
                        	descriptor.setVersion(pluginWrapper.getDescriptor().getVersion());
                        	descriptor.setDescription(pluginWrapper.getDescriptor().getPluginDescription());
                        	descriptor.setPlugInClass(pluginWrapper.getDescriptor().getPluginClass());
                        }

                        LionExtensionWrapper<T> extensionWrapper = new LionExtensionWrapper<>(descriptor);
                        extensionWrapper.setExtensionFactory(extensionFactory);
                        result.add(extensionWrapper);
                        log.debug("Added extension '{}' with ordinal {}", className, extension.ordinal());
                    } else {
                        log.debug("'{}' is not an extension.", className);
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        if (entries.isEmpty()) {
        	log.debug("No extensions found for plugin '{}'", pluginIdLookup);
        } else {
        	log.debug("Found {} extensions for plugin '{}'", entries.size(), pluginIdLookup);
        }

        // sort by "ordinal" property
//        Collections.sort(result);

		return result;
	}

    @Override
    public Set<String> findClassNames(String pluginId) {
    	readIndexFiles();
        return entries.get(pluginId);
    }

    @Override
	public void pluginStateChanged(PluginStateEvent event) {
        // TODO optimize (do only for some transitions)
        // clear cache
        entries = null;
    }

    private Map<String, Set<String>> readIndexFiles() {
        // checking cache
        if (entries != null) {
            return entries;
        }

        entries = new LinkedHashMap<>();

        readClasspathIndexFiles();
        readPluginsIndexFiles();

        return entries;
    }

    private void readClasspathIndexFiles() {
        log.debug("Reading extensions index files from classpath");

        Set<String> bucket = new HashSet<>();
        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources(ExtensionsIndexer.EXTENSIONS_RESOURCE);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                log.debug("Read '{}'", url.getFile());
                Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
                ExtensionsIndexer.readIndex(reader, bucket);
            }

            if (bucket.isEmpty()) {
                log.debug("No extensions found");
            } else {
                log.debug("Found possible {} extensions:", bucket.size());
                for (String entry : bucket) {
                    log.debug("   " + entry);
                }
            }

            entries.put(null, bucket);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void readPluginsIndexFiles() {
        log.debug("Reading extensions index files from plugins");

        List<PluginWrapper> plugins = pluginManager.getPlugins();
        for (PluginWrapper plugin : plugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            log.debug("Reading extensions index file for plugin '{}'", pluginId);
            Set<String> bucket = new HashSet<>();

            try {
                URL url = plugin.getPluginClassLoader().getResource(ExtensionsIndexer.EXTENSIONS_RESOURCE);
                if (url != null) {
                    log.debug("Read '{}'", url.getFile());
                    Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
                    ExtensionsIndexer.readIndex(reader, bucket);
                } else {
                    log.debug("Cannot find '{}'", ExtensionsIndexer.EXTENSIONS_RESOURCE);
                }

                if (bucket.isEmpty()) {
                    log.debug("No extensions found");
                } else {
                    log.debug("Found possible {} extensions:", bucket.size());
                    for (String entry : bucket) {
                        log.debug("   " + entry);
                    }
                }

                entries.put(pluginId, bucket);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private boolean isExtensionPoint(Class<?> type) {
        return ExtensionPoint.class.isAssignableFrom(type);
    }

	public <T> List<LionExtensionWrapper<T>> findByType(Class<T> type) {
        log.debug("Checking extension point '{}'", type.getName());
        if (!isExtensionPoint(type)) {
            log.warn("'{}' is not an extension point", type.getName());

            return Collections.emptyList(); // or return null ?!
        }

		log.debug("Finding extensions for extension point '{}'", type.getName());
        readIndexFiles();

        List<LionExtensionWrapper<T>> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : entries.entrySet()) {
            String pluginId = entry.getKey();

            PluginWrapper pluginWrapper = null;
            if (pluginId != null) {
                pluginWrapper = pluginManager.getPlugin(pluginId);
                if (PluginState.STARTED != pluginWrapper.getPluginState()) {
                    continue;
                }
            }

            Set<String> extensionClassNames = entry.getValue();

            for (String className : extensionClassNames) {
                try {
                    ClassLoader classLoader;
                    if (pluginId != null) {
                        classLoader = pluginManager.getPluginClassLoader(pluginId);
                    } else {
                        classLoader = getClass().getClassLoader();
                    }
                    log.debug("Loading class '{}' using class loader '{}'", className, classLoader);
                    Class<?> extensionClass = classLoader.loadClass(className);

                    log.debug("Checking extension type '{}'", className);
                    if (type.isAssignableFrom(extensionClass) && extensionClass.isAnnotationPresent(Extension.class)) {
                        Extension extension = extensionClass.getAnnotation(Extension.class);
                        LionExtensionDescriptor descriptor = new LionExtensionDescriptor();
                        descriptor.setOrdinal(extension.ordinal());
                        descriptor.setExtensionClass(extensionClass);
                        descriptor.setPluginId(pluginId);
                        if (pluginWrapper != null) {
                        	descriptor.setVersion(pluginWrapper.getDescriptor().getVersion());
                        	descriptor.setDescription(pluginWrapper.getDescriptor().getPluginDescription());
                        	descriptor.setPlugInClass(pluginWrapper.getDescriptor().getPluginClass());
                        }

                        LionExtensionWrapper<T> extensionWrapper = new LionExtensionWrapper<>(descriptor);
                        extensionWrapper.setExtensionFactory(extensionFactory);
                        result.add(extensionWrapper);
                        log.debug("Added extension '{}' with ordinal {}", className, extension.ordinal());
                    } else {
                        log.debug("'{}' is not an extension for extension point '{}'", className, type.getName());
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        if (entries.isEmpty()) {
        	log.debug("No extensions found for extension point '{}'", type.getName());
        } else {
        	log.debug("Found {} extensions for extension point '{}'", entries.size(), type.getName());
        }

        // sort by "ordinal" property
//        Collections.sort(result);

		return result;
	}

	@Override
	public <T> List<ExtensionWrapper<T>> find(Class<T> type) {
		throw new RuntimeException("Not implemented. Use findByType() instead.");
	}

}
