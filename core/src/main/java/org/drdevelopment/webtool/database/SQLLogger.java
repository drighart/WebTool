package org.drdevelopment.webtool.database;

import org.drdevelopment.webtool.configuration.Config;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.SQLLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLLogger implements SQLLog {
	    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
	    private Integer thresshold = null;
	    
		@Override
		public void logBeginTransaction(Handle h) {
		}

		@Override
		public void logCommitTransaction(long time, Handle h) {
		}

		@Override
		public void logRollbackTransaction(long time, Handle h) {
		}

		@Override
		public void logObtainHandle(long time, Handle h) {
		}

		@Override
		public void logReleaseHandle(Handle h) {
		}

		private int getThresshold() {
			if (thresshold == null) {
				thresshold = Config.getConfig().getDatabaseLoggingSQLThressholdInMs();
				LOGGER.info("Log SQL when query exceeds {}ms.", thresshold);
			}
			return thresshold;
		}
		
		@Override
		public void logSQL(long time, String sql) {
			if (getThresshold() > 0 && getThresshold() < time) {
				LOGGER.debug("time {}ms: {}", time, sql);
			}
		}

		@Override
		public void logPreparedBatch(long time, String sql, int count) {
		}

		@Override
		public BatchLogger logBatch() {
			return null;
		}

		@Override
		public void logCheckpointTransaction(Handle h, String name) {
		}

		@Override
		public void logReleaseCheckpointTransaction(Handle h, String name) {
		}

		@Override
		public void logRollbackToCheckpoint(long time, Handle h, String checkpointName) {
		}
}
