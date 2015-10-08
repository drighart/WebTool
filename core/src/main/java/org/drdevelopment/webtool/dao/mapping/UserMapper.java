package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class UserMapper implements ResultSetMapper<User> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public User map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
    	String email = resultSet.getString("EMAIL_ADDRESS");
    	String displayName = resultSet.getString("DISPLAY_NAME");
    	String credentials = resultSet.getString("CREDENTIALS");
    	String roles = resultSet.getString("ROLES");
    	Boolean activated = resultSet.getBoolean("ACTIVATED");
    	String language = resultSet.getString("LANGUAGE");
    	User user = new User(email, displayName, roles, language);
    	user.setCredentials(credentials);
    	user.setCreated(created);
    	user.setModified(modified);
    	user.setActivated(activated);
    	return user; 
    }

}
