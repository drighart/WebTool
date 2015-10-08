package org.drdevelopment.webtool;

import java.io.File;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.database.Database;
import org.drdevelopment.webtool.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private void run(String[] args) {
		try {
			//		Startup.checkLogger();
			
			if (options(args)) {
				Config.getConfig();
				Startup.initTemplateEngine();
				Startup.initWebserver();
				Startup.installShutdownHook();
				Startup.checkFolders();
				Startup.startDatabase();
				Plugins.getPlugins().load();
				Startup.startWebserver();

				while (ShutdownHook.isRunning()) {
					Thread.sleep(1000);
				}
			}
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			shutdown();
		}
	}
	
	private void shutdown() {
		new ShutdownHook().run();
	}
	
	private boolean options(String[] args) throws ParseException, SQLException {
		Options options = new Options();

		options.addOption("h", false, "show help");
		options.addOption("e", false, "export database to sql-file at the same location as where the database is located");
//		options.addOption("i", false, "import sql-file to a new schema");
//		options.addOption("c", false, "compact the database");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		boolean startSystem = false;
	    try {
	    	if (cmd.hasOption("h")) {
	    		HelpFormatter formatter = new HelpFormatter();
	    		formatter.printHelp(Main.class.getPackage().getName() + "." + Main.class.getSimpleName(), options);
	    		return false;
//	    	} else if (cmd.hasOption("c")) {
//	    		LOGGER.info("Compacting database.");
//	    		Database.compact();
	    	} else if (cmd.hasOption("e")) {
	    	    String fileName = Config.getConfig().getDataFolder() + File.separator + "export.sql";
	    		LOGGER.info("Exporting database to file {}.", fileName);
	    		Database.export(fileName);
	    	} else {
	    		startSystem = true;
	    	}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	    
	    if (!startSystem) {
	    	shutdown();
	    }

		return startSystem;
	}
	
	public static void main(String[] args) throws Exception {
		LOGGER.info("Start application.");
		new Main().run(args);
		LOGGER.info("End application.");
	}

}
