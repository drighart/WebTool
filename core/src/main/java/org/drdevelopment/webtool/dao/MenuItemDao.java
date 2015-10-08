package org.drdevelopment.webtool.dao;

import org.drdevelopment.webtool.dao.mapping.MenuItemsMapper;
import org.drdevelopment.webtool.model.MenuItem;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface MenuItemDao {
    
    @SqlQuery("SELECT * FROM DATA.MENU_ITEM WHERE position = :position")
    @Mapper(MenuItemsMapper.class)
    MenuItem find(@Bind("position") Integer position);
    
    @SqlUpdate("UPDATE DATA.MENU_ITEM SET NAME = :name, ON_CURRENT_PAGE = :onCurrentPage, PAGE_ID = :pageId, MODIFIED = NOW() WHERE position = :position")
    void update(@Bind("position") Integer position, @Bind("name") String name, @Bind("onCurrentPage") Boolean onCurrentPage, @Bind("pageId") Integer pageId);

    @SqlUpdate("INSERT INTO DATA.MENU_ITEM (ID, CREATED, MODIFIED, NAME) VALUES "
    		+ "((SELECT MAX(POSITION)+1 FROM DATA.MENU_ITEM), NOW(), NOW(), :name)")
    void newPage(@Bind("name") String name);

    @SqlUpdate("DELETE FROM DATA.MENU_ITEM WHERE position = :position")
    void remove(@Bind("position") Integer position);
    
    void close();
}

