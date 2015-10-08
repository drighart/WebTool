package org.drdevelopment.webtool.plugin.model.dao;

import java.util.List;

import org.drdevelopment.webtool.plugin.authentication.User;
import org.drdevelopment.webtool.plugin.model.dao.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface UserDao {
    
    @SqlQuery("SELECT NAME, ROLES, GROUPS, FULL_NAME, EMAIL_ADDRESS FROM DATA.USERS WHERE LOWER(NAME)=LOWER(:name)")
    @Mapper(UserMapper.class)
    User findUser(@Bind("name") String name);

    @SqlQuery("SELECT NAME, ROLES, GROUPS, FULL_NAME, EMAIL_ADDRESS FROM DATA.USERS")
    @Mapper(UserMapper.class)
    List<User> findUsers();

    @SqlQuery("SELECT NAME, ROLES, GROUPS, FULL_NAME, EMAIL_ADDRESS FROM DATA.USERS WHERE LOWER(NAME)=LOWER(:name) AND PASSWORD=:hashPassword")
    @Mapper(UserMapper.class)
    User checkPassword(@Bind("name") String name, @Bind("hashPassword") String hashPassword);
    
    @SqlUpdate("INSERT INTO DATA.USERS (NAME, PASSWORD, ROLES, GROUPS, FULL_NAME, EMAIL_ADDRESS) values (:name, :hashPassword, 'USER', 'USER', :fullName, :email)")
    void createNewUser(@Bind("name") String name, @Bind("hashPassword") String hashPassword, @Bind("fullName") String fullName, @Bind("email") String email);
    
    @SqlUpdate("UPDATE DATA.USERS SET ROLES=:roles WHERE NAME=:name")
    void updateRoles(@Bind("name") String name, @Bind("roles") String roles);
    
    /**
     * close with no args is used to close the connection
     */
    void close();
}

