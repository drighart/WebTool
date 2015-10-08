package org.drdevelopment.webtool.plugin.api.model;

import java.util.List;

public interface PageItem {

	String getName();

	String getTitle();

	List<ParagraphItem> getParagraphs();
}
