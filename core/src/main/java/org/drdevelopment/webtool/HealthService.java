package org.drdevelopment.webtool;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.drdevelopment.webtool.plugin.api.PluginHealthService;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.model.H2DbSession;
import org.drdevelopment.webtool.plugin.Plugins;
import org.drdevelopment.webtool.repository.H2DbRepository;
import org.drdevelopment.webtool.repository.HealthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HealthService.class);

    private static HealthService healthService;
    private boolean status = true;
    private Exception statusException = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new HealthCheckerThreadFactory());
	
    private HealthService() {
    	scheduler.scheduleAtFixedRate(new HealthChecker(), 1, Config.getConfig().getHealthCheckInterval(), TimeUnit.SECONDS);
	}

	public synchronized static HealthService getHealthService() {
		if (healthService == null) {
			healthService = new HealthService();
		}
		return healthService;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public Exception getStatusException() {
		return statusException;
	}

	private class HealthChecker implements Runnable {

		@Override
		public void run() {
			try {
				// check if the database is still available.
				LOGGER.trace("Checking health status.");
				status = HealthRepository.check();
				statusException = null;
				
				int activeConnections = Services.getServices().getJdbcConnectionPool().getActiveConnections();
				int maxConnections = Services.getServices().getJdbcConnectionPool().getMaxConnections();
				if (activeConnections > maxConnections - 3) {
					LOGGER.warn("The database is busy! Number of active connections is {} from max available {}", 
							activeConnections, maxConnections);
				}
				
//				List<H2DbSession> sessions = H2DbRepository.getSessions();
//				for (H2DbSession session : sessions) {
//					LOGGER.debug("{}", session.toString());
//				}
				
				if (status) {
					List<PluginHealthService> healthServices = Plugins.getPlugins().getHealthServices();
					for (PluginHealthService healthService : healthServices) {
						Exception exception = healthService.getStatus();
						if (exception != null) {
							status = false;
							statusException = exception;
							break;
						}
					}
				}
			} catch (Exception e) {
				status = false;
				statusException = e;
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	private class HealthCheckerThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable runnable) {
			return new Thread(runnable, "health-checker");
		}
	}
}
