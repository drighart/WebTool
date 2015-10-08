package org.drdevelopment.webtool.rest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.model.Image;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.drdevelopment.webtool.repository.ImageRepository;
import org.drdevelopment.webtool.repository.MenuItemRepository;
import org.drdevelopment.webtool.rest.data.ApplyFilter;
import org.drdevelopment.webtool.rest.data.ImageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/image")
public class ImageRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageRest.class);

	@GET
    @Path("get/{name}")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getImage(@PathParam("name") String name) {
		Image image = ImageRepository.find(name);
		ImageItem imageItem = null;
		if (image != null) {
			imageItem = new ImageItem(image.getName(), image.getTags());
			fillHeightWidth(imageItem);
		}
		return Response.status(Response.Status.OK).entity(imageItem).build();
	}

	private void fillHeightWidth(ImageItem imageItem) {
		String fileName = Config.getConfig().getImagesFolder() + File.separator + imageItem.getName();
		if (FileUtil.isFileExists(fileName)) {
			try {
				BufferedImage image = ImageIO.read(new File(fileName));
				imageItem.setHeight(image.getHeight());
				imageItem.setWidth(image.getWidth());
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
	}
	
	@POST
    @Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(ImageItem image) {
		LOGGER.debug("Update image {} with tags '{}'", image.getName(), image.getTags());
		
		ImageRepository.update(image.getName(), image.getTags());
		return Response.status(Response.Status.OK).build();
	}

	@POST
    @Path("applyfilter")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response applyFilter(ApplyFilter applyFilter) {
		LOGGER.debug("Apply filter on image {} with filter '{}' and postfix {}", applyFilter.getName(), applyFilter.getFilter(),
				applyFilter.getPostfix());
		ImageRepository.filter(applyFilter.getName(), applyFilter.getTags(), applyFilter.getPostfix(), applyFilter.getFilter());
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
    @Path("remove/{position}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("position") Integer position) {
		LOGGER.debug("Remove menu-item with position '{}'", position);
		
		MenuItemRepository.remove(position);
		return Response.status(Response.Status.OK).build();
	}

}
