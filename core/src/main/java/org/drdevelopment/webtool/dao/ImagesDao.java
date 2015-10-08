package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.ImageMapper;
import org.drdevelopment.webtool.model.Image;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface ImagesDao {
    
    @SqlQuery("SELECT * FROM DATA.IMAGE WHERE name = :name")
    @Mapper(ImageMapper.class)
    Image find(@Bind("name") String name);

    @SqlQuery("SELECT * FROM DATA.IMAGE")
    @Mapper(ImageMapper.class)
    List<Image> find();

    @SqlUpdate("INSERT INTO DATA.IMAGE (CREATED, MODIFIED, NAME, TAGS) VALUES "
    		+ "(NOW(), NOW(), :name, :tags)")
    void insert(@Bind("name") String name, @Bind("tags") String tags);

    @SqlUpdate("DELETE FROM DATA.IMAGE WHERE name = :name")
    void remove(@Bind("name") String name);

    @SqlUpdate("UPDATE DATA.IMAGE SET TAGS = :tags, MODIFIED = NOW() WHERE name = :name")
    void update(@Bind("name") String name, @Bind("tags") String tags);

    void close();
}

