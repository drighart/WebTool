package org.drdevelopment.webtool.plugin.model;

import java.util.ArrayList;
import java.util.List;

import org.drdevelopment.webtool.plugin.api.model.PageItem;
import org.drdevelopment.webtool.plugin.api.model.ParagraphItem;

public class PageItemImpl implements PageItem {
	private String name;
	private String title;
	private List<ParagraphItem> paragraphs = new ArrayList<>();

	public PageItemImpl(String name, String title) {
		super();
		this.name = name;
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ParagraphItem> getParagraphs() {
		return paragraphs;
	}

	@Override
	public String toString() {
		return "PageItemImpl [name=" + name + ", title=" + title + ", paragraphs=" + paragraphs + "]";
	}
	
}
