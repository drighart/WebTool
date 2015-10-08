package org.drdevelopment.webtool.dao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.drdevelopment.webtool.model.H2DbSession;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * 
 */
public class H2DbSessionsMapper implements ResultSetMapper<H2DbSession> {

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public H2DbSession map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {

        Integer id = resultSet.getInt("ID");
        LocalDateTime sessionStart = resultSet.getTimestamp("SESSION_START").toLocalDateTime();
        LocalDateTime statementStart = resultSet.getTimestamp("STATEMENT_START").toLocalDateTime();
        String username = resultSet.getString("USER_NAME");
        String statement = resultSet.getString("STATEMENT");

        return new H2DbSession(id, username, sessionStart, statement, statementStart);
    }

}
