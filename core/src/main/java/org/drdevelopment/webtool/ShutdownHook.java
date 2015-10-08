package org.drdevelopment.webtool;

import java.time.Duration;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.database.Database;
import org.drdevelopment.webtool.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);

    private  static boolean running = true;
    
	@Override
	public void run() {
		LOGGER.info("Shutdown initiated.");
		
		Plugins.getPlugins().shutdownPluginServices();
		
		if (Services.getServices().getWebServer() != null) {
			try {
				LOGGER.info("Stop webserver");
				Services.getServices().getWebServer().stop();
			} catch (Exception e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		
		Database.stop();
		running = false;
		
		LOGGER.info("Bye bye! The system ran for {}.", 
				getPrettyTimeDuration(System.currentTimeMillis() - Config.getConfig().getStartedOn()));
	}

	public static boolean isRunning() {
		return running;
	}
	
	private String getPrettyTimeDuration(long durationInMs) {
		Duration d = Duration.ofMillis(durationInMs);
		
		long days = d.toDays(); 
		long hours = d.minusDays(days).toHours(); 
		long minutes = d.minusDays(days).minusHours(hours).toMinutes(); 
		long seconds = d.minusDays(days).minusHours(hours).minusMinutes(minutes).toMillis() / 1000; 

		StringBuilder sb = new StringBuilder();
		boolean placeComma = false;
		if (days > 0) {
			sb.append(days + " days");
			placeComma = true;
		}
		if (hours > 0) {
			if (placeComma) {
				sb.append(", ");
			}
			sb.append(hours + " hours");
			placeComma = true;
		}
		if (minutes > 0) {
			if (placeComma) {
				sb.append(", ");
			}
			sb.append(minutes + " minutes");
			placeComma = true;
		}
		if (seconds >= 0) {
			if (placeComma) {
				sb.append(", ");
			}
			sb.append(seconds + " seconds");
		}
		return sb.toString();
	}
	
}
