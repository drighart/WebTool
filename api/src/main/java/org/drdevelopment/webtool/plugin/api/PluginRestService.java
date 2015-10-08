package org.drdevelopment.webtool.plugin.api;

import java.util.Set;

import ro.fortsoft.pf4j.ExtensionPoint;

public interface PluginRestService extends ExtensionPoint {

	Set<Class<?>> getRestServices();

}
