package org.drdevelopment.webtool.dao;

import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface HealthDao {
    
    @SqlQuery("SELECT 1")
    Integer check();

    /**
     * close with no args is used to close the connection
     */
    void close();
}

