/*
 * This software source code is provided by the USEF Foundation. The copyright
 * and all other intellectual property rights relating to all software source
 * code provided by the USEF Foundation (and changes and modifications as well
 * as on new versions of this software source code) belong exclusively to the
 * USEF Foundation and/or its suppliers or licensors. Total or partial
 * transfer of such a right is not allowed. The user of the software source
 * code made available by USEF Foundation acknowledges these rights and will
 * refrain from any form of infringement of these rights.

 * The USEF Foundation provides this software source code "as is". In no event
 * shall the USEF Foundation and/or its suppliers or licensors have any
 * liability for any incidental, special, indirect or consequential damages;
 * loss of profits, revenue or data; business interruption or cost of cover or
 * damages arising out of or in connection with the software source code or
 * accompanying documentation.
 *
 * For the full license agreement see http://www.usef.info/license.
 */

package org.drdevelopment.webtool.model;

import java.time.LocalDateTime;

/**
 * @author righard
 *
 */
public class Paragraph {

	private Integer id;
	private LocalDateTime created;
	private LocalDateTime modified;
	
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

	public Paragraph() {
		super();
	}

	public Paragraph(Integer id, String content, String buttonText, String buttonLink, Integer position, Integer pageId, String template,
			String anchor, String imageName, String imageAlignment, String backgroundColor, String backgroundImage) {
		super();
		this.id = id;
		this.created = LocalDateTime.now();
		this.modified = LocalDateTime.now();
		this.content = content;
		this.buttonText = buttonText;
		this.buttonLink = buttonLink;
		this.position = position;
		this.pageId = pageId;
		this.template = template;
		this.anchor = anchor;
		this.imageName = imageName;
		this.imageAlignment = imageAlignment;
		this.backgroundColor = backgroundColor;
		this.backgroundImage = backgroundImage;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Integer getId() {
		return id;
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

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
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

	public String getButtonLink() {
		return buttonLink;
	}

	public void setButtonLink(String buttonLink) {
		this.buttonLink = buttonLink;
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
	
}
