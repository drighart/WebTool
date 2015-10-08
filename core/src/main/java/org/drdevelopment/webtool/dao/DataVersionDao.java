package org.drdevelopment.webtool.dao;

import org.drdevelopment.webtool.dao.mapping.DataVersionMapper;
import org.drdevelopment.webtool.model.DataVersion;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface DataVersionDao {
    
    @SqlQuery("SELECT VERSION FROM DATA.DATA_VERSION")
    @Mapper(DataVersionMapper.class)
    DataVersion findDataUpdateVersion();

    @SqlUpdate("INSERT INTO DATA.DATA_VERSION (VERSION) values (:version)")
    void insert(@Bind("version") int version);

    @SqlUpdate("UPDATE DATA.DATA_VERSION SET DATA.DATA_VERSION.VERSION=:version")
    void update(@Bind("version") int version);

    void close();
}

