package org.drdevelopment.webtool.plugin.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.plugin.authentication.User;
import org.drdevelopment.webtool.plugin.model.UserImpl;
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
    	String name = resultSet.getString("NAME");
    	String email = resultSet.getString("EMAIL_ADDRESS");
    	String fullName = resultSet.getString("FULL_NAME");
    	String roles = resultSet.getString("ROLES");
    	String groups = resultSet.getString("GROUPS");
    	User user = new UserImpl(name, email, fullName);
    	if (roles != null) {
    		roles = StringUtils.remove(roles, ' ');
    		user.getRoles().addAll(Arrays.asList(roles.toUpperCase().split(",")));
    	}
    	if (groups != null) {
    		groups = StringUtils.remove(groups, ' ');
    		user.getGroups().addAll(Arrays.asList(groups.toUpperCase().split(",")));
    	}
    	return user; 
    }

}
