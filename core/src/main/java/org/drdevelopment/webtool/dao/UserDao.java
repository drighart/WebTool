package org.drdevelopment.webtool.dao;

import org.drdevelopment.webtool.dao.mapping.UserMapper;
import org.drdevelopment.webtool.model.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface UserDao {
    
    @SqlQuery("SELECT * FROM DATA.USER WHERE LOWER(EMAIL_ADDRESS)=LOWER(:emailAddress) AND ACTIVATED = true")
    @Mapper(UserMapper.class)
    User findUser(@Bind("emailAddress") String emailAddress);
    
    @SqlUpdate("UPDATE DATA.USER SET ROLES = :roles, DISPLAY_NAME = :displayName, MODIFIED = NOW() WHERE EMAIL_ADDRESS = :emailAddress")
    void update(@Bind("emailAddress") String emailAddress, @Bind("roles") String roles, @Bind("displayName") String displayName);

    @SqlUpdate("UPDATE DATA.USER SET ACTIVATED = true, MODIFIED = NOW() WHERE EMAIL_ADDRESS = :emailAddress")
    void activate(@Bind("emailAddress") String emailAddress);

    @SqlUpdate("UPDATE DATA.USER SET CREDENTIALS = :credentials WHERE EMAIL_ADDRESS = :emailAddress")
    void changeCredentials(@Bind("emailAddress") String emailAddress, @Bind("credentials") String credentials);

    @SqlUpdate("INSERT INTO DATA.USER (CREATED, MODIFIED, EMAIL_ADDRESS, ROLES, DISPLAY_NAME, LANGUAGE) VALUES "
    		+ "(NOW(), NOW(), :emailAddress, :roles, :displayName, :language)")
    void insert(@Bind("emailAddress") String emailAddress, @Bind("roles") String roles, @Bind("displayName") String displayName, @Bind("language") String language);

    @SqlUpdate("DELETE FROM DATA.USER WHERE EMAIL_ADDRESS = :emailAddress")
    void remove(@Bind("emailAddress") String emailAddress);
    
    void close();
}

