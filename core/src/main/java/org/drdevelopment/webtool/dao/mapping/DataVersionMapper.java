package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.drdevelopment.webtool.model.DataVersion;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class DataVersionMapper implements ResultSetMapper<DataVersion> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public DataVersion map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {

        Integer dataVersion = resultSet.getInt("VERSION");
        return new DataVersion(dataVersion);
    }

}
