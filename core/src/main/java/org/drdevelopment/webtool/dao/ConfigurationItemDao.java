package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.ConfigurationItemMapper;
import org.drdevelopment.webtool.model.ConfigurationItem;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface ConfigurationItemDao {
    
    @SqlQuery("SELECT * FROM DATA.CONFIGURATION_ITEM")
    @Mapper(ConfigurationItemMapper.class)
    List<ConfigurationItem> find();

    @SqlQuery("SELECT * FROM DATA.CONFIGURATION_ITEM WHERE NAME = :name")
    @Mapper(ConfigurationItemMapper.class)
    ConfigurationItem find(@Bind("name") String name);

    @SqlQuery("SELECT * FROM DATA.CONFIGURATION_ITEM WHERE NAME = :name AND PLUGIN_ID = :pluginId")
    @Mapper(ConfigurationItemMapper.class)
    ConfigurationItem find(@Bind("pluginId") String pluginId, @Bind("name") String name);

    @SqlUpdate("UPDATE DATA.CONFIGURATION_ITEM SET VALUE = :value, MODIFIED = NOW() WHERE NAME = :name")
    void save(@Bind("name") String name, @Bind("value") String value);

    @SqlUpdate("UPDATE DATA.CONFIGURATION_ITEM SET VALUE = :value, PLUGIN_ID = :pluginId, TYPE_OF_INPUT = :typeOfInput, MODIFIED = NOW() WHERE NAME = :name")
    void save(@Bind("pluginId") String pluginId, @Bind("typeOfInput") String typeOfInput, @Bind("name") String name, @Bind("value") String value);

    @SqlUpdate("INSERT INTO DATA.CONFIGURATION_ITEM (CREATED, MODIFIED, NAME, VALUE, PLUGIN_ID, TYPE_OF_INPUT) VALUES "
    		+ "(NOW(), NOW(), :name, :value, :pluginId, :typeOfInput)")
    void insert(@Bind("pluginId") String pluginId, @Bind("typeOfInput") String typeOfInput, @Bind("name") String name, @Bind("value") String value);

    void close();
}

