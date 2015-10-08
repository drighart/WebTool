package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.DataVersionMapper;
import org.drdevelopment.webtool.dao.mapping.PluginMapper;
import org.drdevelopment.webtool.model.DataVersion;
import org.drdevelopment.webtool.model.Plugin;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface PluginDataVersionDao {
    
    @SqlQuery("SELECT VERSION FROM DATA.PLUGIN WHERE PLUGIN_ID=:pluginId")
    @Mapper(DataVersionMapper.class)
    DataVersion findDataUpdateVersion(@Bind("pluginId") String pluginId);

    @SqlUpdate("INSERT INTO DATA.PLUGIN (CREATED, MODIFIED, PLUGIN_ID, VERSION, RUNNING) values (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), :pluginId, :version, false)")
    void insert(@Bind("pluginId") String pluginId, @Bind("version") int version);

    @SqlUpdate("UPDATE DATA.PLUGIN SET DATA.PLUGIN.VERSION=:version, MODIFIED=CURRENT_TIMESTAMP(), PLUGIN_ID=:pluginId")
    void update(@Bind("pluginId") String pluginId, @Bind("version") int version);

    @SqlUpdate("UPDATE DATA.PLUGIN SET MODIFIED=CURRENT_TIMESTAMP(), RUNNING=:running")
    void setRunning(@Bind("running") boolean running);

    @SqlUpdate("UPDATE DATA.PLUGIN SET MODIFIED=CURRENT_TIMESTAMP(), RUNNING=:running WHERE PLUGIN_ID=:pluginId")
    void setRunning(@Bind("pluginId") String pluginId, @Bind("running") boolean running);

    @SqlQuery("SELECT * FROM DATA.PLUGIN")
    @Mapper(PluginMapper.class)
    List<Plugin> findPlugings();

    @SqlQuery("SELECT * FROM DATA.PLUGIN WHERE PLUGIN_ID=:pluginId")
    @Mapper(PluginMapper.class)
    Plugin find(@Bind("pluginId") String pluginId);

    void close();
}

