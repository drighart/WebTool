package org.drdevelopment.webtool;

import java.io.StringReader;

import org.drdevelopment.webtool.repository.DataVersionRepository;
import org.drdevelopment.webtool.util.FileUtil;
import org.h2.tools.RunScript;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUpdate {
    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
    private static final int DATAUPDATE_VERSION = 1;
    
	public static void execute() {
        Handle handle = Services.getServices().getDbi().open();
        handle.execute("CREATE TABLE IF NOT EXISTS DATA.DATA_VERSION(VERSION INT);");
        handle.close();
        
        Integer dataUpdateVersion = DataVersionRepository.getDataVersion();
        if (dataUpdateVersion == null) {
        	DataVersionRepository.insert();
        	DataVersionRepository.update(0);
        }
        
        runDataUpdate();
        
        dataUpdateVersion = DataVersionRepository.getDataVersion();
        LOGGER.debug("All data updates are executed and current data update version is {}.", dataUpdateVersion);
	}
	
	private static void runDataUpdate() {
		int dataUpdateVersion = 0;
		try {
			dataUpdateVersion = DataVersionRepository.getDataVersion();
			while (dataUpdateVersion < DATAUPDATE_VERSION) {

				String filename = "/dataupdate/dataupdate" + dataUpdateVersion + ".sql";
				String sql = FileUtil.readLinesWithCarriageReturnFromResource(filename);

				LOGGER.info("Executing data update with version {}.", dataUpdateVersion);
				LOGGER.debug("Executing SQL from file {}:\n{}", filename, sql);
				RunScript.execute(Services.getServices().getDataSource().getConnection(), new StringReader(sql));
				LOGGER.debug("Done running data update for version {}.", dataUpdateVersion);

				dataUpdateVersion++;
				DataVersionRepository.update(dataUpdateVersion);
			}
		} catch(Exception e) {
			LOGGER.error("Error executing data update with version {}.", dataUpdateVersion);
			throw new RuntimeException(e);
		}
	}
	
}
