package org.drdevelopment.webtool;

import java.io.File;
import java.sql.SQLException;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.database.Database;
import org.drdevelopment.webtool.template.TemplateEngine;
import org.drdevelopment.webtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class Startup {
    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
	    
    public static void checkLogger() {
    	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);
    }
    
    public static void checkFolders() {
    	LOGGER.info("Base folder is {}", Config.getConfig().getBaseFolder());
    	checkFolder(Config.getConfig().getDataFolder());
    	checkFolder(Config.getConfig().getImagesFolder());
    	checkFolder(Config.getConfig().getImagesPreviewsFolder());
    	checkFolder(Config.getConfig().getLogFolder());
    	checkFolder(Config.getConfig().getLogFolder() + File.separator + "access");
    	checkFolder(Config.getConfig().getConfigurationFolder());
    	checkFolder(Config.getConfig().getPluginsFolder());
    }

    private static void checkFolder(String folderName) {
    	if (!FileUtil.isFolderExists(folderName)) {
    		LOGGER.info("Folder {} does not exist and will be created.", folderName);
    		FileUtil.createFolders(folderName);
    	}
    }
    
    public static void installShutdownHook() {
    	Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(), "shutdown-thread"));
    }
    
	public static void startDatabase() throws SQLException {
		Database.start();
	}
	
	public static void initWebserver() {
		WebServer webServer = new WebServer();
		Services.getServices().setWebServer(webServer);
	}
	
	public static void startWebserver() {
		Services.getServices().getWebServer().start();
	}
	
	public static void initTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		Services.getServices().setTemplateEngine(templateEngine);
	}

}
