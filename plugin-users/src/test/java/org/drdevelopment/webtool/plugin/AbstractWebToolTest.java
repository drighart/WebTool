package org.drdevelopment.webtool.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.plugin.PluginServices;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractWebToolTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebToolTest.class);

	private DataSource dataSource;

	@Before
    public void setUpDatabase() throws Exception {
        dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test", Config.getConfig().getDatabaseUser(), 
        		Config.getConfig().getDatabasePassword());
        Services.getServices().setDataSource(dataSource);
        PluginServices.getServices().setCoreServices(Services.getServices());
        executeDataupdates();
	}

    @After
    public void tearDown() throws Exception {
		RunScript.execute(dataSource.getConnection(), new StringReader("DROP SCHEMA DATA"));
    }

	private void executeDataupdates() throws IOException, SQLException {
		RunScript.execute(dataSource.getConnection(), new StringReader("CREATE SCHEMA DATA"));
		List<String> sqlFiles = getSQLFiles(getDataupdateFolder());
		for (String sqlFile : sqlFiles) {
			LOGGER.debug("Executing sql from file {}", sqlFile);
			String sql = FileUtil.readFile(sqlFile);
			RunScript.execute(dataSource.getConnection(), new StringReader(sql));
		}
	}
	
	private List<String> getSQLFiles(String folder) {
		assert folder != null;
		List<String> filenames = new ArrayList<String>();
		File dir = new File(folder);
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".sql");
		    }
		});
		
		if (files != null) {
			for (File file : files) {
				if (file != null && folder != null) {
					filenames.add(folder + File.separator + file.getName());
				}
			}
		}
		
		Collections.sort(filenames);
		return filenames;
	}
	
	private String getDataupdateFolder() {
		String folder = Config.getConfig().getBaseFolder() + File.separator + "src" + File.separator + "main" + 
				File.separator + "resources" + File.separator + "dataupdate";
		LOGGER.debug("Dataupdate folder is {}", folder);
		return folder;
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

    private static String parse(String message, Object... values) {
        if (message != null) {
        	for (Object value : values) {
        		message = message.replaceFirst("\\{\\}", value.toString());
        	}
        }
        return message;
    }
	
	protected void executeSQL(String sqlUpdate, Object... values) throws SQLException {
		String sql = parse(sqlUpdate, values);
		LOGGER.debug("Executing sql: {}", sql);
		RunScript.execute(dataSource.getConnection(), new StringReader(sql));
	}
	
	protected String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}
