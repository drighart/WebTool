package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.drdevelopment.webtool.model.Page;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class PageMapper implements ResultSetMapper<Page> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PageMapper.class);

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Page map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
        Integer id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        String title = resultSet.getString("TITLE");
        
        Page page = new Page(id, name, title);
        page.setCreated(created);
        page.setModified(modified);
        return page;
    }

}
