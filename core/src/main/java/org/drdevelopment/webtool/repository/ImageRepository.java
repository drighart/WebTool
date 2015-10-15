package org.drdevelopment.webtool.repository;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.dao.ImagesDao;
import org.drdevelopment.webtool.model.Image;
import org.drdevelopment.webtool.servlet.FileExtenstionMediaType;
import org.drdevelopment.webtool.util.FileUtil;
import org.drdevelopment.webtool.util.ImageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageRepository.class);

	private ImageRepository() {
		// no constructor.
	}
	
	public static List<Image> getImages() {
		ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
        List<Image> list = dao.find();
        Collections.sort(list, new Comparator<Image>() {
			@Override
			public int compare(Image o1, Image o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
        Services.getServices().getDbi().close(dao);
        return list;
	}

	public static void insert(String name, String tags) {
		Image imageExists = find(name);
		ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
		if (imageExists == null) {
			dao.insert(name, tags);
		} else {
			dao.update(name, tags);
		}
		Services.getServices().getDbi().close(dao);
	}
	
	public static Image find(String name) {
		ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
		Image image = dao.find(name);
        Services.getServices().getDbi().close(dao);
        return image;
	}

	public static void update(String name, String tags) {
		ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
		dao.update(name, tags);
        Services.getServices().getDbi().close(dao);
	}

	public static void remove(String name) {
		ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
		dao.remove(name);
        Services.getServices().getDbi().close(dao);
		
		FileUtil.removeFile(Config.getConfig().getImagesFolder() + File.separator + name, false);
		FileUtil.removeFile(Config.getConfig().getImagesPreviewsFolder() + File.separator + name, false);
	}

	public static void filter(String name, String tags, String postfix, String filter) {
		if (postfix == null || postfix.isEmpty()) {
			LOGGER.warn("No postfix, so filter {} can not be applied to image {}.", filter, name);
		} else {

			String fromFile = Config.getConfig().getImagesFolder() + File.separator + name;
			if (!FileUtil.isFileExists(fromFile)) {
				LOGGER.warn("Image with name {} does not exist on disk. The filter will not be applied.", name);
			} else {
				String fileExtension = FileExtenstionMediaType.getFileExtension(fromFile);
				if (!FileExtenstionMediaType.IMAGE.isMediaType(fileExtension)) {
					throw new IllegalArgumentException("No valid image file extension in: " + fromFile);
				}

				filterImage(name, tags, postfix, fromFile, fileExtension);
			}
		}
	}

	public static void filterImage(String name, String tags, String postfix, String fromFile, String fileExtension) {
		try {
			String toFilename = name.substring(0, name.length() - fileExtension.length()) + postfix + fileExtension;
			
			BufferedImage originalImage = ImageIO.read(new File(fromFile));
			BufferedImage targetImage = ImageProcessor.grayscale(originalImage);
			
			ImageIO.write(targetImage, fileExtension.toLowerCase().substring(1), new File(toFilename)); 

			// generate preview
			String imagePreviewFileName = Config.getConfig().getImagesPreviewsFolder() + File.separator + toFilename;
			resizeImage(toFilename, imagePreviewFileName, Config.getConfig().getImagePreviewWidth(), 
					Config.getConfig().getImagePreviewHeight());

			ImagesDao dao = Services.getServices().getDbi().open(ImagesDao.class);
			dao.insert(toFilename, tags);
			Services.getServices().getDbi().close(dao);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public static void writeImage(String fileName, String tags, byte[] bytes) throws IOException {
		String imageFileName = Config.getConfig().getImagesFolder() + File.separator + fileName;
		writeFile(bytes, imageFileName);
		
		String imagePreviewFileName = Config.getConfig().getImagesPreviewsFolder() + File.separator + fileName;
		resizeImage(imageFileName, imagePreviewFileName, Config.getConfig().getImagePreviewWidth(), 
				Config.getConfig().getImagePreviewHeight());
		
		ImageRepository.insert(fileName, tags);
	}

	private static void resizeImage(String fromFile, String toFile, int width, int height) throws IOException {
		if (fromFile == null || fromFile.isEmpty()) {
			throw new IllegalArgumentException("The image file is not set.");
		}
		
		String fileExtension = FileExtenstionMediaType.getFileExtension(fromFile);
		if (!FileExtenstionMediaType.IMAGE.isMediaType(fileExtension)) {
			throw new IllegalArgumentException("No valid image file extension in: " + fromFile);
		}
		
		BufferedImage originalImage = ImageIO.read(new File(fromFile));
		 
		BufferedImage resizeImageJpg = ImageProcessor.scalePerspective(originalImage, 200, 200);
		ImageIO.write(resizeImageJpg, fileExtension.toLowerCase().substring(1), new File(toFile)); 
	}
	
	private static void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		} else {
			LOGGER.warn("File {} already exists. It will be overwritten.", filename);
		}

		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}

}
