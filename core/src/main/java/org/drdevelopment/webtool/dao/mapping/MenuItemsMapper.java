package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.drdevelopment.webtool.model.MenuItem;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class MenuItemsMapper implements ResultSetMapper<MenuItem> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemsMapper.class);

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public MenuItem map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
        Integer pageId = resultSet.getInt("PAGE_ID");
        String name = resultSet.getString("NAME");
        Integer position = resultSet.getInt("POSITION");
        Boolean onCurrentPage = resultSet.getBoolean("ON_CURRENT_PAGE");

        MenuItem menuItem = new MenuItem(position, name, pageId);
        menuItem.setCreated(created);
        menuItem.setModified(modified);
        menuItem.setOnCurrentPage(onCurrentPage);
        return menuItem;
    }

}
