package org.drdevelopment.webtool.dao.mapping;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.io.IOUtils;
import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.model.Paragraph;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class ParagraphMapper implements ResultSetMapper<Paragraph> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphMapper.class);

    /* (non-Javadoc)
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Paragraph map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        LocalDateTime created = resultSet.getTimestamp("CREATED").toLocalDateTime();
        LocalDateTime modified = resultSet.getTimestamp("MODIFIED").toLocalDateTime();
        Integer id = resultSet.getInt("ID");
        Clob content = resultSet.getClob("CONTENT");
        String buttonText = resultSet.getString("BUTTON_TEXT");
        String buttonLink = resultSet.getString("BUTTON_LINK");
        String contentStr = getContent(id, content);
        Integer position = resultSet.getInt("POSITION");
        Integer pageId = resultSet.getInt("PAGE_ID");
        String template = resultSet.getString("TEMPLATE");
        String anchor = resultSet.getString("ANCHOR");
        String imageName = resultSet.getString("IMAGE_NAME");
        String imageAlignment = resultSet.getString("IMAGE_ALIGNMENT");
        String backgroundColor = resultSet.getString("BACKGROUND_COLOR");
        String backgroundImage = resultSet.getString("BACKGROUND_IMAGE");

        Paragraph paragraph = new Paragraph(id, contentStr, buttonText, buttonLink, position, pageId, template, anchor,
        		imageName, imageAlignment, backgroundColor, backgroundImage);
        paragraph.setCreated(created);
        paragraph.setModified(modified);
        return paragraph;
    }

	public String getContent(Integer id, Clob content) throws SQLException {
		String contentStr = null;
		try {
			if (content != null && content.getCharacterStream() != null) {
				contentStr = new String(IOUtils.toCharArray(content.getCharacterStream()));
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.warn("Could not retrieve content of the paragraph with id {}.", id);
		}
		return contentStr;
	}

}
