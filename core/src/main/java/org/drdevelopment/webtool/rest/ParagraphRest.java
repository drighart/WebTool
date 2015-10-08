package org.drdevelopment.webtool.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.Paragraph;
import org.drdevelopment.webtool.repository.ParagraphRepository;
import org.drdevelopment.webtool.rest.data.ParagraphDataRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/paragraph")
public class ParagraphRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphRest.class);

	@GET
    @Path("list/{pageId}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getParagraphs(@PathParam("pageId") Integer pageId) {
		List<Paragraph> paragraphs = ParagraphRepository.getParagraphs(pageId);
		List<ParagraphDataRest> paragraphsDataRest = new ArrayList<>();
		int i = 0;
		for (Paragraph paragraph : paragraphs) {
			ParagraphDataRest paragraphDataRest = new ParagraphDataRest(paragraph.getId(), paragraph.getContent(), 
					paragraph.getButtonText(), paragraph.getButtonLink(),
					paragraph.getPageId(), paragraph.getPosition(), paragraph.getTemplate(), paragraph.getAnchor());
			if (i == 0) {
				paragraphDataRest.setFirst(true);
			} 
			if (i == paragraphs.size() - 1) {
				paragraphDataRest.setLast(true);
			}
			paragraphsDataRest.add(paragraphDataRest);
			i++;
		}
		return Response.status(Response.Status.OK).entity(paragraphsDataRest).build();
	}

	@GET
    @Path("get/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getParagraph(@PathParam("id") Integer id) {
		Paragraph paragraph = ParagraphRepository.find(id);
		return Response.status(Response.Status.OK).entity(paragraph).build();
	}

	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(Paragraph paragraph) {
		LOGGER.debug("Update paragraph with id {}", paragraph.getId());
		
		ParagraphRepository.update(paragraph.getId(), paragraph.getContent(), paragraph.getButtonText(), paragraph.getButtonLink(),
				paragraph.getPosition(), paragraph.getTemplate(), paragraph.getAnchor(), paragraph.getImageName(), 
				paragraph.getImageAlignment(), paragraph.getBackgroundColor(), paragraph.getBackgroundImage());
		return Response.status(Response.Status.OK).build();
	}

	@POST
    @Path("new/{pageId}/{template}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response newParagraph(@PathParam("pageId") Integer pageId, @PathParam("template") String template) {
		LOGGER.debug("New paragraph with template '{}' and pageId {}", template, pageId);
		
		ParagraphRepository.insert(pageId, template);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("id") Integer id) {
		LOGGER.debug("Remove paragraph with id '{}'", id);
		
		ParagraphRepository.remove(id);
		return Response.status(Response.Status.OK).build();
	}

	@PUT
    @Path("up/{pageId}/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response up(@PathParam("pageId") Integer pageId, @PathParam("position") Integer position) {
		LOGGER.debug("Moving up paragraph with position '{}'", position);
		
		ParagraphRepository.up(pageId, position);
		return Response.status(Response.Status.OK).build();
	}

	@PUT
    @Path("down/{pageId}/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response down(@PathParam("pageId") Integer pageId, @PathParam("position") Integer position) {
		LOGGER.debug("Moving down paragraph with position '{}'", position);
		
		ParagraphRepository.down(pageId, position);
		return Response.status(Response.Status.OK).build();
	}

}
