package org.drdevelopment.webtool.plugin;

import java.util.HashSet;
import java.util.Set;

import org.drdevelopment.webtool.plugin.api.PluginRestService;
import org.drdevelopment.webtool.plugin.rest.ServiceRest;
import org.drdevelopment.webtool.plugin.rest.TimeRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class ExampleRestServiceExtension implements PluginRestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleRestServiceExtension.class);

	@Override
	public Set<Class<?>> getRestServices() {
		// TODO You can use reflection to obtain the rest services automatically.
		Set<Class<?>> classes = new HashSet<>();
		classes.add(ServiceRest.class);
		classes.add(TimeRest.class);
		return classes;
	}

}
