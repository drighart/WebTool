package org.drdevelopment.webtool.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.PagesDao;
import org.drdevelopment.webtool.model.Page;

public class PagesRepository {

	private PagesRepository() {
		// no constructor.
	}
	
	public static List<Page> getPages() {
		PagesDao dao = Services.getServices().getDbi().open(PagesDao.class);
        List<Page> list = dao.find();
        Collections.sort(list, new Comparator<Page>() {
			@Override
			public int compare(Page o1, Page o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
        Services.getServices().getDbi().close(dao);
        return list;
	}

	public static Map<Integer, Page> getPagesAsMap() {
		List<Page> pages = getPages();
		Map<Integer, Page> pagesAsMap = new HashMap<>();
		for (Page page : pages) {
			pagesAsMap.put(page.getId(), page);
		}
        return pagesAsMap;
	}

}
