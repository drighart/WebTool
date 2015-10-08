package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.drdevelopment.webtool.model.Page;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class PagesMapper implements ResultSetMapper<Page> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Page map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        String title = resultSet.getString("TITLE");
        Page page = new Page(id, name, title);
        return page;
    }

}
