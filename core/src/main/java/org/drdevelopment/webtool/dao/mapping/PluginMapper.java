package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.drdevelopment.webtool.model.Plugin;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class PluginMapper implements ResultSetMapper<Plugin> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Plugin map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        Integer dataVersion = resultSet.getInt("VERSION");
        String pluginId = resultSet.getString("PLUGIN_ID");
    	Boolean running = resultSet.getBoolean("RUNNING");

        return new Plugin(pluginId, dataVersion, running);
    }

}
