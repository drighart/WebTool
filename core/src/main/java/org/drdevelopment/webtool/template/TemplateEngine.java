package org.drdevelopment.webtool.template;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.api.Template;
import org.drdevelopment.webtool.api.TemplateProcessor;
import org.drdevelopment.webtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);

	private Map<String, TemplateImpl> templates = new ConcurrentHashMap<>();
	private Map<String, TemplateProcessor> templateProcessors = new ConcurrentHashMap<>();
	
	public void register(String uri) {
		if (StringUtils.isEmpty(uri)) {
			throw new IllegalArgumentException("Uri is null or empty.");
		}
		TemplateImpl template = newTemplate(uri);
		if (template != null) {
			LOGGER.debug("Register template with uri {}", uri);
			templates.put(uri.replace('\\', '/'), template);
		}
	}

	public void register(TemplateProcessor templateProcessor) {
		if (templateProcessor == null || StringUtils.isEmpty(templateProcessor.getTag())) {
			throw new IllegalArgumentException("Template processor is null or tag in template processor is null or empty.");
		}
		
		LOGGER.debug("Register template with tag {}", templateProcessor.getTag());
		templateProcessors.put(templateProcessor.getTag().toLowerCase(), templateProcessor);
	}

	private Template getOrCreateTemplate(String uri) {
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("Uri is null or empty.");
		}
		TemplateImpl template = templates.get(uri.replace('\\', '/'));

		if (template == null) {
			template = newTemplate(uri.replace('\\', '/'));
			if (template != null) {
				templates.put(uri, template);
			}
		}

		return template;
	}

	public String get(String uri, String param) {
		Template template = getOrCreateTemplate(uri);
		if (template == null) {
			return null;
		} else {
			return template.get(uri, param);
		}
	}
		
	public String get(String uri, Map<String, String> values, String param) {
		Template template = getOrCreateTemplate(uri);
		return template.get(values, uri, param);
	}
	
	private TemplateImpl newTemplate(String uri) {
		Path path = Paths.get(uri);
		if (path == null) {
			LOGGER.warn("Could not find uri {}.", uri);
		} else {
			String source = readSource(path.toString());
			if (source == null) {
				LOGGER.warn("Could not find source from path {}", path.toString());
			} else {
				TemplateImpl template = new TemplateImpl(this, uri.replace('\\', '/'));
				template.parse(source);
				return template;
			}
		}
		return null;
	}
	
	private String readSource(String uri) {
		String source = null;
		assert uri != null;
		try {
			if (FileUtil.isResourceExists(uri)) {
				source = FileUtil.readLinesWithCarriageReturnFromResource(uri);
			} else if (FileUtil.isFileExists(uri)) {
				source = FileUtil.readFile(uri);
			} else {
				if (uri != null && !uri.toLowerCase().endsWith("template")) {
					LOGGER.error("Can not find file or resource with uri {}.", uri);
				}
			}
		} catch(IOException e) {
			LOGGER.warn("Could not find resource with uri {}", uri);
		}
		return source;
	}
	
	public TemplateImpl getTemplate(String uri) {
		return templates.get(uri);
	}

	public Map<String, TemplateProcessor> getTemplateProcessors() {
		return templateProcessors;
	}
	
}
