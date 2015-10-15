package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.drdevelopment.webtool.model.Image;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class ImageMapper implements ResultSetMapper<Image> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageMapper.class);

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Image map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
        String name = resultSet.getString("NAME");
        String tags = resultSet.getString("TAGS");

        Image image = new Image(name, tags);
        image.setCreated(created);
        image.setModified(modified);
        return image;
    }

}
