package org.drdevelopment.webtool.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.drdevelopment.webtool.servlet.FileExtenstionMediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhlabs.image.BorderFilter;
import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.BumpFilter;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.CropFilter;
import com.jhlabs.image.GammaFilter;
import com.jhlabs.image.GrayFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.PosterizeFilter;
import com.jhlabs.image.RaysFilter;
import com.jhlabs.image.RotateFilter;
import com.jhlabs.image.ScaleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.SharpenFilter;

public class ImageProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);

	private ImageProcessor() {
		// private constructor. So can not be initiated.
	}
	
	public static BufferedImage blur(BufferedImage image) {
		if (image != null) {
			BoxBlurFilter filter = new BoxBlurFilter(5, 5, 2);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage scale(BufferedImage image, int width, int height) {
		if (image != null) {
			ScaleFilter filter = new ScaleFilter(width, height);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return resize(destination, width, height);
		} else {
			return null;
		}
	}

	public static BufferedImage scalePerspective(BufferedImage image, int width, int height) {
		if (image != null) {
			int oldWidth = image.getWidth();
			int oldHeight = image.getHeight();
			int newWidth = 0;
			int newHeight = 0;
			
			if (oldWidth > oldHeight) {
				newWidth = width;
				newHeight = (int) Math.round(1.0 * width / oldWidth * oldHeight);
			} else {
				newWidth = (int) Math.round(1.0 * height / oldHeight * oldWidth);
				newHeight = height;
			}

			ScaleFilter filter = new ScaleFilter(newWidth, newHeight);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return resize(destination, newWidth, newHeight);
		} else {
			return null;
		}
	}
	
	public static BufferedImage scale(BufferedImage image, double percentage) {
		if (image != null) {
			int height = (int) Math.round(image.getHeight() * percentage);
			int width = (int) Math.round(image.getWidth() * percentage);
			ScaleFilter filter = new ScaleFilter(width, height);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return resize(destination, width, height);
		} else {
			return null;
		}
	}
	
	public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
		if (image != null) {
			CropFilter filter = new CropFilter(x, y, width, height);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return resize(destination, width, height);
		} else {
			return null;
		}
	}
	
	public static BufferedImage gray(BufferedImage image) {
		if (image != null) {
			GrayFilter filter = new GrayFilter();
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage contrast(BufferedImage image, float brightness, float contrast) {
		if (image != null) {
			ContrastFilter filter = new ContrastFilter();
			filter.setBrightness(brightness);
			filter.setContrast(contrast);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage grayscale(BufferedImage image) {
		if (image != null) {
			GrayscaleFilter filter = new GrayscaleFilter();
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage rotate(BufferedImage image, float angle, boolean resize) {
		if (image != null) {
			RotateFilter filter = new RotateFilter(angle, resize);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage boxBlur(BufferedImage image, float hRadius, float vRadius, int iterations) {
		if (image != null) {
			BoxBlurFilter filter = new BoxBlurFilter(hRadius, vRadius, iterations);
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}
	
	public static BufferedImage posterize(BufferedImage image, Integer numLevels) {
		if (image != null) {
			PosterizeFilter filter = new PosterizeFilter();
			if (numLevels > 0) {
				filter.setNumLevels(numLevels);
			}
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	
	public static BufferedImage resize(BufferedImage source, int width, int height) { 
	    BufferedImage destination = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	    Graphics2D g2d = destination.createGraphics();
	    g2d.drawImage(source, 0, 0, null);
	    g2d.dispose();

	    return destination;
	}  
	
	public static Response imageToResponse(String fileName, BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
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

	}

	public static BufferedImage sharpen(BufferedImage image) {
		if (image != null) {
			SharpenFilter filter = new SharpenFilter();
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage bump(BufferedImage image) {
		if (image != null) {
			BumpFilter filter = new BumpFilter();
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage shadow(BufferedImage image) {
		if (image != null) {
			ShadowFilter filter = new ShadowFilter();
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage gamma(BufferedImage image, Float gamma) {
		if (image != null) {
			GammaFilter filter = new GammaFilter();
			if (gamma != null && gamma >= 0) {
				filter.setGamma(gamma);
			}
			BufferedImage destination = filter.createCompatibleDestImage(image, null);
			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	public static BufferedImage border(BufferedImage image, int borderWidth, Color borderColor) {
		if (image != null) {
			Paint paint = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue());
			BorderFilter filter = new BorderFilter(borderWidth, borderWidth, borderWidth, borderWidth, paint);
			BufferedImage destination = new BufferedImage(image.getWidth() + 2 * borderWidth, image.getHeight() + 2 * borderWidth, image.getType());

			filter.filter(image, destination);
			return destination;
		} else {
			return null;
		}
	}

	
	public static BufferedImage readImage(String fileName) {
		BufferedImage image = null;
		if (FileUtil.isFileExists(fileName)) {
			try {
				image = ImageIO.read(new File(fileName));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.warn("Image file {} can not be found.", fileName);
		}
		return image;
	}

}
