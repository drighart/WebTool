package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.MenuItemsMapper;
import org.drdevelopment.webtool.model.MenuItem;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface MenuItemsDao {
    
    @SqlQuery("SELECT * FROM DATA.MENU_ITEM")
    @Mapper(MenuItemsMapper.class)
    List<MenuItem> find();

    @SqlUpdate("INSERT INTO DATA.MENU_ITEM (CREATED, MODIFIED, POSITION, NAME, PAGE_ID) VALUES "
    		+ "(NOW(), NOW(), (SELECT MAX(POSITION)+1 FROM DATA.MENU_ITEM), :name, 0)")
    void newMenuItem(@Bind("name") String name);
    
    @SqlUpdate("DELETE FROM DATA.MENU_ITEM WHERE name = :name")
    void remove(@Bind("name") String name);

    @SqlUpdate("UPDATE DATA.MENU_ITEM SET POSITION = :newPosition WHERE POSITION = :oldPosition")
    void updatePosition(@Bind("oldPosition") Integer oldPosition, @Bind("newPosition") Integer newPosition);

    void close();
}

