package org.drdevelopment.webtool.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.util.FileUtil;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final String CONFIGURATION_DEFAULT_PROPERTIES = "/configuration/default.properties";

	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
	
    private static Config config;
    private Properties properties;
    private MailConfig mailConfig;
    private String baseFolder;
    private long started;
    
    private OperatingSystemMXBean operatingSystemMXBean;
    
	private Config() {
		started = System.currentTimeMillis();
		getBaseFolder();
		readProperties();
		mailConfig = new MailConfig(properties);
	}

	public static Config getConfig() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}
	
	public MailConfig getMailConfig() {
		return mailConfig;
	}

	public long getStartedOn() {
		return started;
	}
	
	public String getBaseFolder() {
		if (baseFolder == null) {
			baseFolder = FileUtil.getCurrentFolder();
		}
		return baseFolder;
	}

	public boolean isDevelopmentMode() {
		return "true".equalsIgnoreCase(properties.getProperty(ConfigParam.DEVELOPMENT_MODE.name()));
	}
	
	public String getDataFolder() {
		return getBaseFolder() + File.separator + "data";
	}

	public String getImagesFolder() {
		return getDataFolder() + File.separator + "images";
	}

	public String getImagesPreviewsFolder() {
		return getImagesFolder() + File.separator + "previews";
	}
	
	public String getConfigurationFolder() {
		return getBaseFolder() + File.separator + "configuration";
	}

	public String getLogFolder() {
		return getBaseFolder() + File.separator + "log";
	}

	public String getPluginsFolder() {
		return getBaseFolder() + File.separator + "plugins";
	}

	public int getDatabasePort() {
		return getIntegerValue(ConfigParam.DB_PORT);
	}

	public int getDatabaseConsolePort() {
		return getIntegerValue(ConfigParam.DB_CONSOLE_PORT);
	}

	public String getDatabaseUrl() {
		Server dbServer = Services.getServices().getDatabase();
		if (dbServer == null) {
			LOGGER.error("Database-server is not running.");
			return null;
		}
		String dbUrl = "jdbc:h2:" + dbServer.getURL() + "/" + getDataFolder() + File.separator + "data;";
		if (!isDatabaseCipherPasswordEmpty()) {
			dbUrl += "CIPHER=AES;";
		}
		return dbUrl + "MVCC=true";
	}

	public String getDatabaseUser() {
		return properties.getProperty(ConfigParam.DB_USERNAME.name());
	}

	public String getDatabasePassword() {
		return properties.getProperty(ConfigParam.DB_PASSWORD.name());
	}

	public boolean isDatabaseCipherPasswordEmpty() {
		return getDatabaseCipherPassword() == null || getDatabaseCipherPassword().isEmpty();
	}
	
	public String getDatabaseCipherPassword() {
		return properties.getProperty(ConfigParam.DB_CIPHER_PASSWORD.name());
	}

	public boolean getDatabaseLoggingSQL() {
		return Boolean.parseBoolean(properties.getProperty(ConfigParam.DB_SQL_LOGGING.name()));
	}

	public int getDatabaseLoggingSQLThressholdInMs() {
		return getIntegerValue(ConfigParam.DB_SQL_LOGGING_THRESSHOLD_IN_MS);
	}

	public int getWebserverHttpPort() {
		return getIntegerValue(ConfigParam.WEBSERVER_HTTP_PORT);
	}

	public int getWebserverHttpsPort() {
		return getIntegerValue(ConfigParam.WEBSERVER_HTTPS_PORT);
	}
	
	public String getWebserverKeystoreFilename() {
		return properties.getProperty(ConfigParam.WEBSERVER_KEYSTORE_FILENAME.name());
	}
	
	public int getImagePreviewWidth() {
		return getIntegerValue(ConfigParam.IMAGE_PREVIEW_WIDTH);
	}

	public int getImagePreviewHeight() {
		return getIntegerValue(ConfigParam.IMAGE_PREVIEW_HEIGHT);
	}

	public int getHealthCheckInterval() {
		return getIntegerValue(ConfigParam.HEALTH_CHECK_INTERVAL_IN_SECONDS);
	}
	
	/**
	 * @return only on Linux a valid value for load. Else it will be negative.
	 */
	public double getSystemLoadAverage() {
		return getOperatingSystemMXBean().getSystemLoadAverage();
	}

	private int getIntegerValue(ConfigParam configParam) {
		String value = properties.getProperty(configParam.name());
		if (value == null || value.isEmpty()) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}
	
	private void readProperties() {
		String propertyFileFromDisk = getBaseFolder() + File.separator + "configuration" + 
				File.separator + "configuration.properties";
		properties = FileUtil.readPropertiesFromResource(CONFIGURATION_DEFAULT_PROPERTIES);

		if (FileUtil.isFileExists(propertyFileFromDisk)) {
			Properties propFromFile = FileUtil.readProperties(propertyFileFromDisk);

			// merge properties
			for (Entry<Object, Object> entry : propFromFile.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}
		} else {
			LOGGER.info("No extra properties on disk available. File {} does not exist. The file will be created from the default settings.", propertyFileFromDisk);
			try {
				String configuration = FileUtil.readLinesWithCarriageReturnFromResource(CONFIGURATION_DEFAULT_PROPERTIES);
				FileUtil.writeTextToFile(propertyFileFromDisk, configuration);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
        StringBuilder sb = new StringBuilder();
        Map<Object, Object> sorted = new TreeMap<Object, Object>(properties);
        for (Entry<Object, Object> entry : sorted.entrySet()) {
        	String key = (String) entry.getKey();
        	String value = (String) entry.getValue();
        	if (key != null && key.toLowerCase().contains("password")) {
        		value = StringUtils.leftPad("", value.length(), '*');
        	}
            sb.append("\n" + key + "=" + value);
        }
        LOGGER.debug("Using the following properties:{}", sb.toString());
	}
	
    private OperatingSystemMXBean getOperatingSystemMXBean() {
        if (operatingSystemMXBean == null) {
            operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        }
        return operatingSystemMXBean;
    }

}
