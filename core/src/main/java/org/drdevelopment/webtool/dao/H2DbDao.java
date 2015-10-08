package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.H2DbSessionsMapper;
import org.drdevelopment.webtool.model.H2DbSession;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface H2DbDao {
    
    @SqlQuery("select * from information_schema.sessions")
    @Mapper(H2DbSessionsMapper.class)
    List<H2DbSession> getSessions();

    /**
     * close with no args is used to close the connection
     */
    void close();
}

