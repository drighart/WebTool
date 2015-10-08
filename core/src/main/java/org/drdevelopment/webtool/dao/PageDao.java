package org.drdevelopment.webtool.dao;

import org.drdevelopment.webtool.dao.mapping.PageMapper;
import org.drdevelopment.webtool.model.Page;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface PageDao {
    
    @SqlQuery("SELECT * FROM DATA.PAGE WHERE id = :id")
    @Mapper(PageMapper.class)
    Page find(@Bind("id") Integer id);

    @SqlQuery("SELECT * FROM DATA.PAGE WHERE name = :name")
    @Mapper(PageMapper.class)
    Page find(@Bind("name") String name);

    @SqlUpdate("UPDATE DATA.PAGE SET TITLE = :title, MODIFIED = NOW() WHERE id = :id")
    void update(@Bind("id") Integer id, @Bind("title") String title);

    @SqlUpdate("INSERT INTO DATA.PAGE (CREATED, MODIFIED, NAME, TITLE, POSITION) VALUES "
    		+ "(NOW(), NOW(), :name, :title, (SELECT MAX(POSITION)+1 FROM DATA.PAGE))")
    void insert(@Bind("name") String name, @Bind("title") String title);

    @SqlUpdate("DELETE FROM DATA.PAGE WHERE id = :id")
    void remove(@Bind("id") Integer id);
    
    void close();
}

