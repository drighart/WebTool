package org.drdevelopment.webtool.plugin.model;

import java.util.ArrayList;
import java.util.List;

import org.drdevelopment.webtool.plugin.api.model.PageItem;
import org.drdevelopment.webtool.plugin.api.model.PagesItem;

public class PagesItemImpl implements PagesItem {
	private List<PageItem> pages = new ArrayList<>();

	public PagesItemImpl() {
		super();
	}

	public List<PageItem> getPages() {
		return pages;
	}

	@Override
	public String toString() {
		return "PagesItemImpl [pages=" + pages + "]";
	}
	
}
