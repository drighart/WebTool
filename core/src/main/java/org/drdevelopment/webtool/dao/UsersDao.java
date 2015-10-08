package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.UserMapper;
import org.drdevelopment.webtool.model.User;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface UsersDao {
    
    @SqlQuery("SELECT * FROM DATA.USER")
    @Mapper(UserMapper.class)
    List<User> find();

    /**
     * close with no args is used to close the connection
     */
    void close();
}

