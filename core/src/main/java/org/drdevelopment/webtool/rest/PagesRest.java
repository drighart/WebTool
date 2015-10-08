package org.drdevelopment.webtool.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.model.Paragraph;
import org.drdevelopment.webtool.repository.PagesRepository;
import org.drdevelopment.webtool.repository.ParagraphRepository;
import org.drdevelopment.webtool.rest.data.PageDataRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/pages")
public class PagesRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PagesRest.class);

	@GET
    @Path("list")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getPages() {
		List<PageDataRest> pagesDataRest = new ArrayList<>();
		List<Page> pages = PagesRepository.getPages();
		
		int i = 0;
		for (Page page : pages) {
			PageDataRest pageDataRest = new PageDataRest(page.getId(), page.getName(), page.getTitle());
			pagesDataRest.add(pageDataRest);
		}
		
//		Map<Integer, List<Paragraph>> paragraphsPerPage = ParagraphRepository.getParagraphPerPage();
//		
//		int j = 0;
//		for (Page page : pages) {
//			PageDataRest pageParagraph = new PageDataRest(page.getId(), page.getName(), page.getTitle());
//			if (j == 0) {
//				pageParagraph.setFirst(true);
//			} 
//			if (j == pages.size() - 1) {
//				pageParagraph.setLast(true);
//			}
//			pageParagraphs.add(pageParagraph);
//			
//			List<Paragraph> paragraphs = paragraphsPerPage.get(page.getId());
//			if (paragraphs != null) {
//				int i = 0;
//				for (Paragraph paragraph : paragraphs) {
//					pageParagraph = new PageDataRest(paragraph.getPosition(), paragraph.getTemplate(), paragraph.getPageId());
//					if (i == 0) {
//						pageParagraph.setFirst(true);
//					} 
//					if (i == paragraphs.size() - 1) {
//						pageParagraph.setLast(true);
//					}
//					pageParagraphs.add(pageParagraph);
//					i++;
//				}
//			}
//		}
		
		return Response.status(Response.Status.OK).entity(pagesDataRest).build();
	}

}
