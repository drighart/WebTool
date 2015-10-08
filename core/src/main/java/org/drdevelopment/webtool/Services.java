package org.drdevelopment.webtool;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.drdevelopment.webtool.model.ConfigurationItem;
import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.model.Paragraph;
import org.drdevelopment.webtool.plugin.CoreServices;
import org.drdevelopment.webtool.plugin.api.model.PageItem;
import org.drdevelopment.webtool.plugin.api.model.PagesItem;
import org.drdevelopment.webtool.plugin.api.model.ParagraphItem;
import org.drdevelopment.webtool.plugin.model.PageItemImpl;
import org.drdevelopment.webtool.plugin.model.PagesItemImpl;
import org.drdevelopment.webtool.plugin.model.ParagraphItemImpl;
import org.drdevelopment.webtool.repository.ConfigurationItemRepository;
import org.drdevelopment.webtool.repository.PagesRepository;
import org.drdevelopment.webtool.repository.ParagraphRepository;
import org.drdevelopment.webtool.template.TemplateEngine;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Services implements CoreServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(Services.class);

	private static Services services;

	private org.h2.tools.Server database;
	private DataSource dataSource;
	private DBI dbi;
	private JdbcConnectionPool jdbcConnectionPool;
	private org.h2.tools.Server databaseConsole;
	private WebServer webServer;
	private TemplateEngine templateEngine;
	
	private Services() {
	}

	public static Services getServices() {
		if (services == null) {
			services = new Services();
		}
		return services;
	}

	public org.h2.tools.Server getDatabase() {
		return database;
	}

	public DBI getDbi() {
		return dbi;
	}

	public void setDbi(DBI dbi) {
		this.dbi = dbi;
	}
	
	public JdbcConnectionPool getJdbcConnectionPool() {
		return jdbcConnectionPool;
	}

	public void setJdbcConnectionPool(JdbcConnectionPool jdbcConnectionPool) {
		this.jdbcConnectionPool = jdbcConnectionPool;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDatabase(org.h2.tools.Server database) {
		this.database = database;
	}

	public org.h2.tools.Server getDatabaseConsole() {
		return databaseConsole;
	}

	public void setDatabaseConsole(org.h2.tools.Server databaseConsole) {
		this.databaseConsole = databaseConsole;
	}

	public WebServer getWebServer() {
		return webServer;
	}

	public void setWebServer(WebServer webServer) {
		this.webServer = webServer;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public String getConfigurationItem(String name) {
		ConfigurationItem ci = ConfigurationItemRepository.findByName(name);
		if (ci == null) {
			LOGGER.warn("Could not find configuration item with name {}.", name);
			return null;
		} else {
			return ci.getValue();
		}
	}

	@Override
	public PagesItem getPages() {
		List<Page> pages = PagesRepository.getPages();
		Map<Integer, List<Paragraph>> paragraphsMap = ParagraphRepository.getParagraphPerPage();

		PagesItem pagesItem = new PagesItemImpl();
		for (Page page : pages) {
			PageItem pageItem = new PageItemImpl(page.getName(), page.getTitle());
			pagesItem.getPages().add(pageItem);
			
			List<Paragraph> paragraphs = paragraphsMap.get(page.getId());
			if (paragraphs == null) {
				LOGGER.warn("Could not find any paragraphs for page with id {}", page.getId());
			} else {
				for (Paragraph paragraph : paragraphs) {
					ParagraphItem paragraphItem = new ParagraphItemImpl(paragraph.getContent(), paragraph.getButtonText(), 
							paragraph.getButtonLink(), page.getId(), paragraph.getPosition(), paragraph.getTemplate(), 
							paragraph.getAnchor(), paragraph.getImageName(), paragraph.getImageAlignment(), 
							paragraph.getBackgroundColor(), paragraph.getBackgroundImage());
					pageItem.getParagraphs().add(paragraphItem);
				}
			}
		}
		
		return pagesItem;
	}
	
}
