package org.drdevelopment.webtool.rest.unsecured;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.drdevelopment.webtool.servlet.FileExtenstionMediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/images")
public class ImageDisplayRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDisplayRest.class);

    @GET
    @Path("get/{name}")
    public Response get(@PathParam("name") String name) {
    	return read(Config.getConfig().getImagesFolder() + File.separator + name);
    }

    @GET
    @Path("get/preview/{name}")
    public Response getPreview(@PathParam("name") String name) {
    	return read(Config.getConfig().getImagesPreviewsFolder() + File.separator + name);
    }

	private Response read(String fileName) {
//		LOGGER.debug("Retrieving image with name {}", fileName);
		
		if (FileUtil.isFileExists(fileName)) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				BufferedImage image = ImageIO.read(new File(fileName));
				String fileExtension = FileExtenstionMediaType.getFileExtension(fileName);
				String contentType = null;
				if (fileExtension == null || fileExtension.isEmpty()) {
					contentType = "image/png";
					fileExtension = "png";
				} else {
					contentType = FileExtenstionMediaType.getContentType(fileExtension);
					fileExtension = fileExtension.substring(1);
				}
				ImageIO.write(image, fileExtension, baos);

				byte[] imageData = baos.toByteArray();

				return Response.status(Response.Status.OK).entity(imageData).type(contentType).build();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			LOGGER.warn("Could not retrieve image with name {}", fileName);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
}
