package org.drdevelopment.webtool.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.drdevelopment.webtool.DataUpdate;
import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.util.FileUtil;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
	private static final String DATABASE_FILENAME = "data.mv.db";

	private Database() {
	}
	
	public static void start() throws SQLException {
		// start the TCP Server
		org.h2.tools.Server server = org.h2.tools.Server.createTcpServer("-tcpPort", "" + Config.getConfig().getDatabasePort(), 
				"-tcpAllowOthers").start();
		LOGGER.info("Database started at host {}.",server.getURL());
		Services.getServices().setDatabase(server);

		String databaseFilename = Config.getConfig().getDataFolder() + File.separator + DATABASE_FILENAME;
		if (!FileUtil.isFileExists(databaseFilename)) {
			LOGGER.debug("Database does not exist and will be created.");
		}

		String dbUrl = getDatabaseUrl();
		String dbPasswd = getDatabasePassword();

		LOGGER.debug("Connecting to database on url {} with user {} and passwd ****.", dbUrl,
				Config.getConfig().getDatabaseUser());
		JdbcConnectionPool connectionPool = JdbcConnectionPool.create(dbUrl, Config.getConfig().getDatabaseUser(), dbPasswd);
		connectionPool.setLoginTimeout(30);
		connectionPool.setMaxConnections(20);
		
		LOGGER.info("Set database log-timeout to {} seconds.", connectionPool.getLoginTimeout());
		LOGGER.info("Set max connections to {}.", connectionPool.getMaxConnections());
		DataSource datasource = JdbcConnectionPool.create(dbUrl, Config.getConfig().getDatabaseUser(), dbPasswd);
		DBI dbi = new DBI(datasource);
		
		if (Config.getConfig().getDatabaseLoggingSQL()) {
			LOGGER.info("Turn on SQL logging for the database. It will be written in logfiles where the name starts with 'SQL'.");
			dbi.setSQLLog(new SQLLogger());
		}
		Services.getServices().setJdbcConnectionPool(connectionPool);
		Services.getServices().setDataSource(datasource);
		Services.getServices().setDbi(dbi);

		// Execute the data-update mechanism.
		DataUpdate.execute();

		if (Config.getConfig().getDatabaseConsolePort() != 0) {
			org.h2.tools.Server databaseConsole = org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", 
					"-webPort", "" + Config.getConfig().getDatabaseConsolePort()).start();
			Services.getServices().setDatabaseConsole(databaseConsole);
			LOGGER.info("Database webserver console started at host {}.", databaseConsole.getURL());
		}
	}
	
	public static void stop() {
		if (Services.getServices().getDatabaseConsole() != null) {
			LOGGER.info("Stop database console");
			Services.getServices().getDatabaseConsole().shutdown();
		}

		if (Services.getServices().getDatabase() != null) {
			LOGGER.info("Stop database");
			Services.getServices().getDatabase().shutdown();
		}
	}
	
	private static String getDatabaseUrl() {
		return Config.getConfig().getDatabaseUrl() + ";INIT=CREATE SCHEMA IF NOT EXISTS DATA;DB_CLOSE_DELAY=-1";
	}
	
	private static String getDatabasePassword() {
		String dbPasswd = Config.getConfig().getDatabasePassword();
		if (!Config.getConfig().isDatabaseCipherPasswordEmpty()) {
			dbPasswd = Config.getConfig().getDatabaseCipherPassword() + " " + dbPasswd;
		}
		return dbPasswd;
	}
	
	public static void compact() throws Exception {
	    String fileName = Config.getConfig().getDataFolder() + File.separator + "backup.sql";
	    export(fileName);
//	    FileUtil.renameFile(Config.getConfig().getDataFolder() + File.separator + DATABASE_FILENAME + ".old", 
//	    		Config.getConfig().getDataFolder() + File.separator + DATABASE_FILENAME);
	    DeleteDbFiles.execute(Config.getConfig().getDataFolder(), "data", false);
	    RunScript.execute(getDatabaseUrl(), Config.getConfig().getDatabaseUser(), getDatabasePassword(), fileName, null, false);
	}

	public static void export(String fileName) throws Exception {
	    Class.forName("org.h2.Driver");

		org.h2.tools.Server server = org.h2.tools.Server.createTcpServer("-tcpPort", "" + Config.getConfig().getDatabasePort()).start();
		Services.getServices().setDatabase(server);
		
	    Connection connection = DriverManager.getConnection(getDatabaseUrl() + ";FILE_LOCK=NO", Config.getConfig().getDatabaseUser(), getDatabasePassword());

	    Script.process(connection, fileName, "", "");
	    
	    server.stop();
	    
	    Thread.sleep(1000);
	    LOGGER.info("Still running is {}.", server.isRunning(true));
	    
	    server.shutdown();
	}


}
