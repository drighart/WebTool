package org.drdevelopment.webtool.rest.data;

import java.time.LocalDateTime;

public class ParagraphDataRest {

	private Integer id;
	
	private String content;
	private String buttonText;
	private String buttonLink;
	private Integer pageId;
	private Integer position;
	private String template;
	private String anchor;
	
	// Field to help to show chevron for changing positions
	private Boolean first = false;
	private Boolean last = false;
	
	public ParagraphDataRest(Integer id, String content, String buttonText, String buttonLink, Integer pageId, Integer position,
			String template, String anchor) {
		super();
		this.id = id;
		this.content = content;
		this.buttonText = buttonText;
		this.buttonLink = buttonLink;
		this.pageId = pageId;
		this.position = position;
		this.template = template;
		this.anchor = anchor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
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

}
