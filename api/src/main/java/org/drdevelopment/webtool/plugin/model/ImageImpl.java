package org.drdevelopment.webtool.plugin.model;

import java.io.IOException;

import org.drdevelopment.webtool.plugin.api.model.Image;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageImpl implements Image {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageImpl.class);
	
	private String name;
	private String tag;
	private byte[] data;
	private String resourceLocation;
	
	public ImageImpl(String name, String tag, String resourceLocation) {
		super();
		this.name = name;
		this.tag = tag;
		this.resourceLocation = resourceLocation;
		getData();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public byte[] getData() {
		if (data == null) {
			try {
				if (FileUtil.isResourceExists(resourceLocation)) {
					data = FileUtil.readBytesFromResource(resourceLocation);
				} else if (FileUtil.isFileExists(resourceLocation)) {
					data = FileUtil.readBytes(resourceLocation);
				} else {
					LOGGER.warn("Resource or file {} does not exist.", resourceLocation);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return data;
	}

	@Override
	public String toString() {
		return "ImageImpl [name=" + name + ", tag=" + tag + ", resourceLocation=" + resourceLocation + "]";
	}
	
}
