package org.drdevelopment.webtool.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.drdevelopment.webtool.api.TemplateProcessor;
import org.drdevelopment.webtool.plugin.api.PluginInitServices;
import org.drdevelopment.webtool.plugin.api.model.ConfigurationItem;
import org.drdevelopment.webtool.plugin.api.model.Image;
import org.drdevelopment.webtool.plugin.api.model.PageItem;
import org.drdevelopment.webtool.plugin.api.model.PagesItem;
import org.drdevelopment.webtool.plugin.api.model.ParagraphItem;
import org.drdevelopment.webtool.plugin.model.ConfigurationItemImpl;
import org.drdevelopment.webtool.plugin.model.ConfigurationTypeOfInput;
import org.drdevelopment.webtool.plugin.model.ImageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Version;

@Extension
public class AppInitExtension implements PluginInitServices {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppInitExtension.class);

	@Override
	public void init(String pluginId, String pluginDescription, String pluginClass, Version pluginVersion,
			DataSource dataSource) {
		LOGGER.info("INIT PLUGIN: {} - {} version {}", pluginId, pluginDescription, pluginVersion);
	}

	@Override
	public List<TemplateProcessor> getProcessors() {
		List<TemplateProcessor> templateProcessors = new ArrayList<>();
		
		templateProcessors.add(createMenuTemplateProcessor());
		templateProcessors.add(createConfigurationItemTemplateProcessor("websiteName", "Website name"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("websiteTitle", "Website title"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("websiteSubtitle", "Website sub-title"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("primaryColor", "Website primary color"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("buttonPressColor", "Website button press color"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("borderColor", "Website border color"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("headerImage", "Website header image"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("headerColor", "Website header color"));
		templateProcessors.add(createConfigurationItemTemplateProcessor("textFadedColor", "Website text faded color"));

		templateProcessors.add(createPageTemplateProcessor("pageContent"));
		templateProcessors.add(createDynamicPageTemplateProcessor());
		templateProcessors.add(createContactTemplateProcessor());
		
		return templateProcessors;
	}

	@Override
	public List<ConfigurationItem> getConfigurationItems() {
		List<ConfigurationItem> configurationItems = new ArrayList<>();
		configurationItems.add(new ConfigurationItemImpl("Website primary color", "#f05f40", ConfigurationTypeOfInput.COLOR));
		configurationItems.add(new ConfigurationItemImpl("Website button press color", "#EE4B28", ConfigurationTypeOfInput.COLOR));
		configurationItems.add(new ConfigurationItemImpl("Website border color", "#ed431f", ConfigurationTypeOfInput.COLOR));
		configurationItems.add(new ConfigurationItemImpl("Website header color", "#fff", ConfigurationTypeOfInput.COLOR));
		configurationItems.add(new ConfigurationItemImpl("Website text faded color", "rgba(255,255,255,.7)", ConfigurationTypeOfInput.COLOR));
		configurationItems.add(new ConfigurationItemImpl("Website header image", "header.jpg", ConfigurationTypeOfInput.TEXT));
		
		return configurationItems;
	}

	@Override
	public Set<Image> getResourceImagesToCopy() {
		Set<Image> images = new HashSet<>();
		images.add(new ImageImpl("header.jpg", "header", AppThemePlugin.RESOURCE_LOCATION + "/app/img/header.jpg"));
		return images;
	}
	
	private TemplateProcessor createMenuTemplateProcessor() {
		return new TemplateProcessor() {
			@Override
			public String getTag() {
				return "topmenu";
			}

			@Override
			public String execute(String source, String uri, String param) {
				StringBuilder sb = new StringBuilder();
				
				PagesItem pages = PluginServices.getServices().getPages();

				boolean firstPage = true;
				String title = "";
				String url = "";
				for (PageItem page : pages.getPages()) {
					
					if (hasParagraphAnchors(page)) {
						if (firstPage) {
							for (ParagraphItem paragraph : page.getParagraphs()) {
								if (paragraph.getAnchor() != null && !paragraph.getAnchor().isEmpty()) {
									url = "#" + paragraph.getAnchor();
									title = paragraph.getAnchor();
									addMenuItem(sb, url, title);
								}
							}
						} else {
							for (ParagraphItem paragraph : page.getParagraphs()) {
								title = paragraph.getAnchor();
								url = "/app/page/" + page.getName() + "#" + paragraph.getAnchor();
								addMenuItem(sb, url, title);
							}
						}
					} else {
						if (firstPage) {
							title = page.getTitle();
							url = "#";
						} else {
							title = page.getTitle();
							url = "/app/page/" + page.getName();
						}
						addMenuItem(sb, url, title);
					}
					
					
//						if (isLandingPage(uri)) {
//							sb.append("<li><a class=\"page-scroll\" href=\"#" + url + "\">" + menuItem.getName() + "</a></li>");
//						} else {
//							sb.append("<li><a class=\"page-scroll\" href=\"/app/#" + url + "\">" + menuItem.getName() + "</a></li>");
//						}
//					} else {
//						sb.append("<li><a class=\"page-scroll\" href=\"/app/page/" + url + "\">" + menuItem.getName() + "</a></li>");
//					}
					firstPage = false;
				}

				return sb.toString();
			}
						
			private void addMenuItem(StringBuilder sb, String url, String title) {
				sb.append("<li><a class=\"page-scroll\" href=\"" + url + "\">" + title + "</a></li>");
			}				
		
			private boolean hasParagraphAnchors(PageItem page) {
				for (ParagraphItem paragraph : page.getParagraphs()) {
					if (paragraph.getAnchor() != null && !paragraph.getAnchor().isEmpty()) {
						return true;
					}
				}
				return false;
			}
			
			private boolean isLandingPage(String uri) {
				return uri.toLowerCase().contains("/app/index.html");
			}
		};
	}

	private TemplateProcessor createPageTemplateProcessor(final String tag) {
		return new TemplateProcessor() {
			@Override
			public String getTag() {
				return tag;
			}

			@Override
			public String execute(String tagName, String uri, String param) {
				PagesItem pages = PluginServices.getServices().getPages();
				
				if (pages == null || pages.getPages() == null || pages.getPages().size() < 1) {
					return "";
				}
				
				StringBuffer sb = new StringBuffer();
				PageItem page = pages.getPages().get(0);
				for (ParagraphItem paragraph : page.getParagraphs()) {
					String style = "";
					String styleBackgroundColor = "";
					if (hasBackground(paragraph)) {
						style = " style=\"";
						if (hasBackgroundImage(paragraph)) {
							String imageUrl = "/rest/unsecured/images/get/" + paragraph.getBackgroundImage();
							style += "background-image:url('" + imageUrl + "'); background-size : cover;";
						}
						if (hasBackgroundColor(paragraph)) {
							styleBackgroundColor += "style=\"background:" + paragraph.getBackgroundColor() + "\"";
						}
						style += "\"";
					}
					
					if (!style.isEmpty()) {
						sb.append("<div " + style + ">");
					}
					if (paragraph.getAnchor() != null && !paragraph.getAnchor().isEmpty()) {
						sb.append("<section id=\"" + paragraph.getAnchor() + "\"" + styleBackgroundColor + ">");						
					} else {
						sb.append("<section " + styleBackgroundColor + ">");						
					}
					sb.append("<div class=\"container\">");
					sb.append("<div class=\"row\">");
					sb.append("<div class=\"col-lg-8 col-lg-offset-2\"");
//					col-lg-6 col-lg-offset-3
					
					sb.append(">");
	                sb.append("<p class=\"text-faded\">" + paragraph.getContent() + "</p>");
	                sb.append("</div>");
					sb.append("</div>");						
					sb.append("</div>");						
					sb.append("</section>");						
					if (!style.isEmpty()) {
						sb.append("</div>");
					}
	                
	                if (paragraph.getButtonText() != null && paragraph.getButtonText().trim().length() > 0) {
	                	sb.append("<aside class=\"bg-dark\">");
	                	sb.append("<div class=\"container text-center\">");
	                	sb.append("<div class=\"call-to-action\">");
	                	sb.append("<a href=\""
	                			+ paragraph.getButtonLink()
	                			+ "\" class=\"btn btn-default btn-xl wow tada\">"
	                			+ paragraph.getButtonText()
	                			+ "</a>");
	                	sb.append("</div>");
	                	sb.append("</div>");
	                	sb.append("</aside>");
	                }
				}

				return sb.toString();
			}
			
			private boolean hasBackground(ParagraphItem paragraph) {
				return (paragraph.getBackgroundImage() != null && !paragraph.getBackgroundImage().isEmpty()) ||
					(paragraph.getBackgroundColor() != null && !paragraph.getBackgroundColor().isEmpty());
			}

			private boolean hasBackgroundImage(ParagraphItem paragraph) {
				return (paragraph.getBackgroundImage() != null && !paragraph.getBackgroundImage().isEmpty());
			}
			
			private boolean hasBackgroundColor(ParagraphItem paragraph) {
				return (paragraph.getBackgroundColor() != null && !paragraph.getBackgroundColor().isEmpty());
			}

		};
	}

	private TemplateProcessor createContactTemplateProcessor() {
		return new TemplateProcessor() {
			@Override
			public String getTag() {
				return "pageContact";
			}

			@Override
			public String execute(String tagName, String uri, String param) {
				StringBuilder sb = new StringBuilder();
				sb.append("<section id=\"Contact\"><div class=\"container\"><div class=\"row\">");
				
//				sb.append("<div class=\"col-lg-8 col-lg-offset-2 text-center\">");
//				sb.append("<h2 class=\"section-heading\">" + page.getTitle() + "</h2>");
//				sb.append("<hr class=\"primary\">");
//				sb.append("<p>" + page.getContent() + "</p>");
//				sb.append("</div>");

				String phone = PluginServices.getServices().getConfigurationItem("Contact Phone");
				if (phone != null && !phone.isEmpty()) {
	                sb.append("<div class=\"col-lg-4 col-lg-offset-2 text-center\">");
	                sb.append("<i class=\"fa fa-phone fa-3x wow bounceIn\"></i>");
	                sb.append("<p>" + phone + "</p>");
	                sb.append("</div>");
				}

				String email = PluginServices.getServices().getConfigurationItem("Contact Email");
				if (email != null && !email.isEmpty()) {
					sb.append("<div class=\"col-lg-4 text-center\">");
					sb.append("<i class=\"fa fa-envelope-o fa-3x wow bounceIn\" data-wow-delay=\".1s\"></i>");
					sb.append("<p><a href=\"mailto:" + email + "\">" + email + "</a></p>");
					sb.append("</div>");
				}
				
				sb.append("</div></div></section>");

				return sb.toString();
			}
		};
	}

	private TemplateProcessor createDynamicPageTemplateProcessor() {
		return new TemplateProcessor() {
			@Override
			public String getTag() {
				return "dynamicPageContent";
			}

			@Override
			public String execute(String tagName, String uri, String param) {
				StringBuilder sb = new StringBuilder();

				if (param == null) {
					return "";
				}

				if (param.startsWith("/")) {
					param = param.substring(1);
				}
					
//				PageItem page = PluginServices.getServices().getPageItem(param.toUpperCase());
//				if (page == null) {
//					return "";
//				}

//				if (page.getIntroduction() != null && !page.getIntroduction().trim().isEmpty()) {
//					sb.append("<section class=\"bg-primary\">");
//					sb.append("<div class=\"container\">");
//					sb.append("<div class=\"row\">");
//					sb.append("<div class=\"col-lg-8 col-lg-offset-2 text-left\">");
//					sb.append(page.getIntroduction());
//					sb.append("</div>");
//					sb.append("</div>");
//					sb.append("</div>");
//					sb.append("</section>");
//				}

				sb.append("<section>");
				sb.append("<div class=\"container\">");
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col-lg-8 col-lg-offset-2 text-left\">");
//				sb.append(page.getContent());
				sb.append("</div>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("</section>");
				
				return sb.toString();
			}
		};
	}

	
	private TemplateProcessor createConfigurationItemTemplateProcessor(final String tag, final String nameOfConfigurationItem) {
		return new TemplateProcessor() {
			@Override
			public String getTag() {
				return tag;
			}

			@Override
			public String execute(String source, String uri, String param) {
				return PluginServices.getServices().getConfigurationItem(nameOfConfigurationItem);
			}
		};
	}

}
