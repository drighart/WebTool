package org.drdevelopment.webtool.template;

import org.drdevelopment.webtool.api.TemplateProcessor;

public class Block {

	private String source;
	private TemplateProcessor templateProcessor;
	
	public Block(String source) {
		this.source = source;
	}
	
	public Block(String source, TemplateProcessor templateProcessor) {
		this.source = source;
		this.templateProcessor = templateProcessor;
	}

	public String getSource() {
		return source;
	}

	public TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}
	
}
