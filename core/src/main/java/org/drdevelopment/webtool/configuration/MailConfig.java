package org.drdevelopment.webtool.configuration;

import java.util.Properties;

public class MailConfig {
	private static final String SMTP_DEBUG = "SMTP_DEBUG";
	private static final String SMTP_HOST = "SMTP_HOST";
	private static final String SMTP_PORT = "SMTP_PORT";
	private static final String SMTP_USERNAME = "SMTP_USERNAME";
	private static final String SMTP_PASSWORD = "SMTP_PASSWORD";

    private Properties properties;

	public MailConfig(Properties properties) {
		super();
		this.properties = properties;
	}

	public boolean isAuthenticated() {
		return getSmtpUsername() != null && !getSmtpUsername().isEmpty() && getSmtpPassword() != null && !getSmtpPassword().isEmpty();
	}
	
	public int getSmtpPort() {
		return Integer.parseInt(properties.getProperty(SMTP_PORT));
	}

	public String getSmtpHost() {
		return properties.getProperty(SMTP_HOST);
	}
	
	public String getSmtpUsername() {
		return properties.getProperty(SMTP_USERNAME);
	}

	public String getSmtpPassword() {
		return properties.getProperty(SMTP_PASSWORD);
	}

	public boolean isSmtpDebug() {
		return "true".equalsIgnoreCase(properties.getProperty(SMTP_DEBUG));
	}

}
