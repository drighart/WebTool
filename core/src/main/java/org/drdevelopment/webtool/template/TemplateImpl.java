package org.drdevelopment.webtool.template;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.drdevelopment.webtool.api.Template;
import org.drdevelopment.webtool.api.TemplateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateImpl implements Template {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateImpl.class);

	private static final String FILE_TAG = "file:";
	private static final String OPEN_TAG = "{%";
	private static final String CLOSE_TAG = "%}";
	
	private TemplateEngine templateEngine;
	private String uri;

	private List<Block> blocks = new ArrayList<>();

	public TemplateImpl(TemplateEngine templateEngine, String uri) {
		super();
		this.templateEngine = templateEngine;
		this.uri = uri;
	}

	public void parse(String source) {
		blocks.clear();
		while(source != null && !source.isEmpty()) {
			int pos1 = source.indexOf(OPEN_TAG);
			if (pos1 > -1) {
				String part1 = source.substring(0, pos1);
				source = source.substring(pos1 + OPEN_TAG.length());
				
				int pos2 = source.indexOf(CLOSE_TAG);
				if (pos2 > -1) {
					String inside = source.substring(0, pos2).trim().toLowerCase();
					source = source.substring(pos2 + CLOSE_TAG.length());
					
					blocks.add(new Block(part1));
					if (inside != null && !inside.isEmpty()) {
						TemplateProcessor templateProcessor = getTemplateEngine().getTemplateProcessors().get(inside);
						if (templateProcessor == null) {
							LOGGER.warn("Template processor is not defined with name {}.", inside);
						} else {
							blocks.add(new Block(inside, templateProcessor));
						}
					}
				} else {
					throw new IllegalArgumentException("This code from uri " + uri + " can not be parsed or contains "
							+ "parser errors, because the close tag %} can not be found.");
				}
			} else {
				blocks.add(new Block(source));
				source = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.drdevelopment.webtool.template.Template#get()
	 */
	@Override
	public String get(String uri, String param) {
		StringBuilder sb = new StringBuilder();
		for (Block block : blocks) {
			if (block.getTemplateProcessor() == null) {
				sb.append(block.getSource());
			} else {
				if (block.getSource().startsWith(FILE_TAG)) {
					Path path = Paths.get(stripUri() + "/" + block.getSource().substring(FILE_TAG.length())).normalize();

					LOGGER.debug("Getting sub-template with uri {}", path.toString());
					sb.append(templateEngine.get(path.toString(), param));
				} else {
					String source = block.getTemplateProcessor().execute(block.getSource(), uri, param);
					sb.append(source);
				}
			}
		}
		return sb.toString();
	}
	
	private String stripUri() {
		String stripped = null;
		if (uri != null) {
			int pos1 = uri.lastIndexOf('\\');
			int pos2 = uri.lastIndexOf('/');
			int pos = Math.max(pos1, pos2);
			if (pos > 0) {
				stripped = uri.substring(0, pos);
			}
		}
		return stripped;
	}
	
	/* (non-Javadoc)
	 * @see org.drdevelopment.webtool.template.Template#get(java.util.Map)
	 */
	@Override
	public String get(Map<String, String> values, String uri, String param) {
		StringBuilder sb = new StringBuilder();
		for (Block block : blocks) {
			if (block.getTemplateProcessor() != null) {
				if (block.getSource().startsWith(FILE_TAG)) {
					Path path = Paths.get(stripUri() + "/" + block.getSource().substring(FILE_TAG.length())).normalize();

					LOGGER.debug("Getting sub-template with uri {}", path.toString());
					sb.append(templateEngine.get(path.toString(), values, param));
				} else {
					String source = block.getTemplateProcessor().execute(block.getSource(), uri, param);
					sb.append(source);
				}
			} else {
				sb.append(block.getSource());
			}
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.drdevelopment.webtool.template.Template#get(java.util.Map)
	 */
	@Override
	public String get(String uri, Map<String, String> values, String param) {
		Path path = Paths.get(uri).normalize();
		return templateEngine.get(path.toString(), values, param);
	}
	
//	private Map<String, String> createLowerCaseValueMap(Map<String, String> values) {
//		Map<String, String> newValues = new HashMap<String, String>();
//		for (Entry<String, String> entry : values.entrySet()) {
//			newValues.put(entry.getKey().toLowerCase(), entry.getValue());
//		}
//		return newValues;
//	}
	
	public List<Block> getBlocks() {
		return blocks;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}
	
}
