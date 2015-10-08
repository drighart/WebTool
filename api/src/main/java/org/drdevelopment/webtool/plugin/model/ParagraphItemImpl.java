package org.drdevelopment.webtool.plugin.model;

import org.drdevelopment.webtool.plugin.api.model.ParagraphItem;

public class ParagraphItemImpl implements ParagraphItem {
	private String content;
	private String buttonText;
	private String buttonLink;
	private Integer pageId;
	private Integer position;
	private String template;
	private String anchor;
	private String imageName;
	private String imageAlignment;
	private String backgroundColor;
	private String backgroundImage;
	
	public ParagraphItemImpl(String content, String buttonText, String buttonLink, Integer pageId, Integer position,
			String template, String anchor, String imageName, String imageAlignment, String backgroundColor,
			String backgroundImage) {
		super();
		this.content = content;
		this.buttonText = buttonText;
		this.buttonLink = buttonLink;
		this.pageId = pageId;
		this.position = position;
		this.template = template;
		this.anchor = anchor;
		this.imageName = imageName;
		this.imageAlignment = imageAlignment;
		this.backgroundColor = backgroundColor;
		this.backgroundImage = backgroundImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public String getButtonLink() {
		return buttonLink;
	}

	public void setButtonLink(String buttonLink) {
		this.buttonLink = buttonLink;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageAlignment() {
		return imageAlignment;
	}

	public void setImageAlignment(String imageAlignment) {
		this.imageAlignment = imageAlignment;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	@Override
	public String toString() {
		return "ParagraphItemImpl [content=" + content + ", buttonText=" + buttonText + ", buttonLink=" + buttonLink
				+ ", pageId=" + pageId + ", position=" + position + ", template=" + template + ", anchor=" + anchor
				+ ", imageName=" + imageName + ", imageAlignment=" + imageAlignment + ", backgroundColor="
				+ backgroundColor + ", backgroundImage=" + backgroundImage + "]";
	}

}
