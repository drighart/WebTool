package org.drdevelopment.webtool.rest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.model.Image;
import org.drdevelopment.webtool.repository.ImageRepository;
import org.drdevelopment.webtool.util.ImageProcessor;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/images")
public class ImagesRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesRest.class);

	@Context
	private HttpServletResponse httpServletResponse;
	
	@Context
	private SecurityContext securityContext;
	
	@GET
    @Path("list")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getImages() {
		List<Image> list = ImageRepository.getImages();
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@POST
	@Path("upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadFile(MultipartFormDataInput input) {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
//		String tags = retrieveTag(uploadForm);
		for (InputPart inputPart : inputParts) {
			try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String fileName = getFileName(header);
				LOGGER.debug("Upload file {}.", fileName);

				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
				ImageRepository.writeImage(fileName, "", bytes);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
			}
		}
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("generate/{name}{param:.*}")
	public Response generate(@PathParam("name") String name, @PathParam("param") String param) {
		LOGGER.info("name {} and param {}", name, param);
		
		String fileName = Config.getConfig().getImagesFolder() + File.separator + name;
		BufferedImage image = ImageProcessor.readImage(fileName);
		image = ImageProcessor.grayscale(image);
		if (image != null) {
			return ImageProcessor.imageToResponse(fileName, image);
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private String retrieveTag(Map<String, List<InputPart>> uploadForm) {
		List<InputPart> inputPartsTags = uploadForm.get("tags");
		String tags = "";
		for (InputPart inputPart : inputPartsTags) {
			try {
				tags = inputPart.getBodyAsString();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return tags;
	}

	@DELETE
    @Path("remove/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response remove(@PathParam("name") String name) {
		LOGGER.debug("Remove image with name '{}' from database and disk.", name);
		
		ImageRepository.remove(name);
		return Response.status(Response.Status.OK).build();
	}
	
	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

}
