package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.drdevelopment.webtool.model.ConfigurationItem;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class ConfigurationItemMapper implements ResultSetMapper<ConfigurationItem> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public ConfigurationItem map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
        String name = resultSet.getString("NAME");
        String value = resultSet.getString("VALUE");
        String pluginId = resultSet.getString("PLUGIN_ID");
        String typeOfInput = resultSet.getString("TYPE_OF_INPUT");
        ConfigurationItem configurationItem = new ConfigurationItem(name, value, pluginId, typeOfInput);
        configurationItem.setCreated(created);
        configurationItem.setModified(modified);
        return configurationItem;
    }

}
