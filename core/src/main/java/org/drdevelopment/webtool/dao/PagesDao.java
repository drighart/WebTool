package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.PagesMapper;
import org.drdevelopment.webtool.model.Page;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface PagesDao {
    
    @SqlQuery("SELECT * FROM DATA.PAGE")
    @Mapper(PagesMapper.class)
    List<Page> find();

    void close();
}

