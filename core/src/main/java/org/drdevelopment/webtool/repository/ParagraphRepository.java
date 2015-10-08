package org.drdevelopment.webtool.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.ParagraphDao;
import org.drdevelopment.webtool.model.Paragraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParagraphRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphRepository.class);

	private ParagraphRepository() {
		// no constructor.
	}
	
	public static List<Paragraph> getParagraphs() {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
        List<Paragraph> list = dao.find();
        Services.getServices().getDbi().close(dao);
        return list;
	}

	public static List<Paragraph> getParagraphs(Integer pageId) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
        List<Paragraph> list = dao.findByPage(pageId);
        Services.getServices().getDbi().close(dao);
        return list;
	}

	public static Map<Integer, List<Paragraph>> getParagraphPerPage() {
		List<Paragraph> paragraphs = getParagraphs();
		Map<Integer, List<Paragraph>> paragraphsAsMap = new HashMap<>();
		for (Paragraph paragraph : paragraphs) {
			List<Paragraph> paragraphsPerPage = paragraphsAsMap.get(paragraph.getPageId());
			if (paragraphsPerPage == null) {
				paragraphsPerPage = new ArrayList<>();
				paragraphsAsMap.put(paragraph.getPageId(), paragraphsPerPage);
			}
			paragraphsPerPage.add(paragraph);
		}
        return paragraphsAsMap;
	}

	public static Paragraph find(Integer id) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		Paragraph paragraph = dao.find(id);
        Services.getServices().getDbi().close(dao);
        return paragraph;
	}

	public static void update(Integer id, String content, String buttonText, String buttonLink, Integer position, String template,
			String anchor, String imageName, String imageAlignment, String backgroundColor, String backgroundImage) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		dao.update(id, content, buttonText, buttonLink, position, template, anchor, imageName, imageAlignment, backgroundColor, backgroundImage);
        Services.getServices().getDbi().close(dao);
	}

	public static void insert(Integer pageId, String template) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		int position = dao.getMaxPositionOfPage(pageId) + 1;
		LOGGER.debug("position {}", position);
		dao.insert(pageId, position, template);
		Services.getServices().getDbi().close(dao);
	}

	public static void remove(Integer id) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		dao.remove(id);
        Services.getServices().getDbi().close(dao);
	}

	private static int getIndex(Integer position, List<Paragraph> paragraphs) {
		int index = -1;
		for (Paragraph paragraph : paragraphs) {
			index++;
			if (position.equals(paragraph.getPosition())) {
				return index;
			}
		}
		return -1;
	}
	
	public static void up(Integer pageId, Integer position) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		List<Paragraph> paragraphs = getParagraphs(pageId);
		int index = getIndex(position, paragraphs) - 1;
		int currentIndex = getIndex(position, paragraphs);
		if (index >= 0 && currentIndex >= 0) {
			Paragraph paragraph1 = paragraphs.get(index);
			Paragraph paragraph2 = paragraphs.get(currentIndex);
			dao.updatePosition(pageId, paragraph1.getPosition(), -1);
			dao.updatePosition(pageId, paragraph2.getPosition(), paragraph1.getPosition());
			dao.updatePosition(pageId, -1, paragraph2.getPosition());
		} else {
			LOGGER.warn("Changing paragraph item up with position {} not possible.", position);
		}
        Services.getServices().getDbi().close(dao);
	}

	public static void down(Integer pageId, Integer position) {
		ParagraphDao dao = Services.getServices().getDbi().open(ParagraphDao.class);
		List<Paragraph> paragraphs = getParagraphs(pageId);
		int index = getIndex(position, paragraphs) + 1;
		int currentIndex = getIndex(position, paragraphs);
		if (index >= 0 && currentIndex >= 0 && index < paragraphs.size()) {
			Paragraph paragraph1 = paragraphs.get(index);
			Paragraph paragraph2 = paragraphs.get(currentIndex);
			dao.updatePosition(pageId, paragraph1.getPosition(), -1);
			dao.updatePosition(pageId, paragraph2.getPosition(), paragraph1.getPosition());
			dao.updatePosition(pageId, -1, paragraph2.getPosition());
		} else {
			LOGGER.warn("Changing paragraph item down with position {} not possible.", position);
		}
        Services.getServices().getDbi().close(dao);
	}

}
