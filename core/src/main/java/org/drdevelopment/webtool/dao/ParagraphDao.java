package org.drdevelopment.webtool.dao;

import java.util.List;

import org.drdevelopment.webtool.dao.mapping.ParagraphMapper;
import org.drdevelopment.webtool.model.Paragraph;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface ParagraphDao {
    
    @SqlQuery("SELECT * FROM DATA.PARAGRAPH ORDER BY POSITION")
    @Mapper(ParagraphMapper.class)
    List<Paragraph> find();

    @SqlQuery("SELECT * FROM DATA.PARAGRAPH WHERE id = :id")
    @Mapper(ParagraphMapper.class)
    Paragraph find(@Bind("id") Integer id);

    @SqlQuery("SELECT * FROM DATA.PARAGRAPH WHERE PAGE_ID = :pageId ORDER BY POSITION")
    @Mapper(ParagraphMapper.class)
    List<Paragraph> findByPage(@Bind("pageId") Integer pageId);

    @SqlUpdate("UPDATE DATA.PARAGRAPH SET CONTENT = :content, BUTTON_TEXT = :buttonText, POSITION = :position, "
    		+ " TEMPLATE = :template, BUTTON_LINK = :buttonLink, IMAGE_NAME = :imageName, IMAGE_ALIGNMENT = :imageAlignment, "
    		+ " BACKGROUND_COLOR = :backgroundColor, BACKGROUND_IMAGE = :backgroundImage, "
    		+ " ANCHOR = :anchor, MODIFIED = NOW() WHERE id = :id")
    void update(@Bind("id") Integer id, @Bind("content") String content, @Bind("buttonText") String buttonText, 
    		@Bind("buttonLink") String buttonLink,
    		@Bind("position") Integer position, @Bind("template") String template, @Bind("anchor") String anchor,
    		@Bind("imageName") String imageName, @Bind("imageAlignment") String imageAlignment,
    		@Bind("backgroundColor") String backgroundColor, @Bind("backgroundImage") String backgroundImage);

    @SqlUpdate("INSERT INTO DATA.PARAGRAPH (CREATED, MODIFIED, TEMPLATE, POSITION, PAGE_ID) VALUES "
    		+ "(NOW(), NOW(), :template, :position, :pageId)")
    void insert(@Bind("pageId") Integer pageId, @Bind("position") Integer position, @Bind("template") String template);

    @SqlQuery("SELECT MAX(POSITION) FROM DATA.PARAGRAPH WHERE PAGE_ID = :pageId")
    int getMaxPositionOfPage(@Bind("pageId") Integer pageId);
    
    @SqlUpdate("UPDATE DATA.PARAGRAPH SET POSITION = :newPosition WHERE PAGE_ID = :pageId AND POSITION = :oldPosition")
    void updatePosition(@Bind("pageId") Integer pageId, @Bind("oldPosition") Integer oldPosition, @Bind("newPosition") Integer newPosition);
    
    @SqlUpdate("DELETE FROM DATA.PARAGRAPH WHERE id = :id")
    void remove(@Bind("id") Integer id);

    void close();
}
