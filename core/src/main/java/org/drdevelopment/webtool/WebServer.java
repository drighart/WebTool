package org.drdevelopment.webtool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.filter.FirstTimeFilter;
import org.drdevelopment.webtool.plugin.util.FileUtil;
import org.drdevelopment.webtool.rest.application.SecureMessageApplication;
import org.drdevelopment.webtool.rest.application.UnsecuredMessageApplication;
import org.drdevelopment.webtool.servlet.AssetsServlet;
import org.drdevelopment.webtool.servlet.ErrorPageHandler;
import org.drdevelopment.webtool.template.TemplateEngine;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.AsyncNCSARequestLog;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.DoSFilter;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer implements Runnable {
    private static final String WEB_THREAD = "web-thread";
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);
    private static final String MESSAGE_APPLICATION_NAME = SecureMessageApplication.class.getName();
    private static final String UNSECURED_MESSAGE_APPLICATION_NAME = UnsecuredMessageApplication.class.getName();
    private static final String[] WELCOME_FILES = { "index.html" };
    
    private Server server;
    private ServletContextHandler context;
    
    public WebServer() {
		context = new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
        context.setContextPath("/");
        context.setWelcomeFiles(WELCOME_FILES);
//        context.addServlet(new ServletHolder(new SystemRest()), "/rest/system/*");

        context.setErrorHandler(new ErrorPageHandler());
        // In case the we develope locally, the target directory should be added. During production the resource files are used
        // which should be available in the jar-file.
        String loginFolder = FileUtil.getCurrentFolder() + File.separator + "target/classes/web";
        if (!FileUtil.isFolderExists(loginFolder)) {
        	loginFolder = FileUtil.getCurrentFolder() + File.separator + "classes/web";
        }
//        context.addServlet(new ServletHolder(new AssetsServlet(loginFolder)), "/");

//        context.addFilter(FirstTimeFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
//        context.addFilter(DoSFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        
//        context.addServlet(new ServletHolder(new HelloServlet("TEST")), "/app/page/*");

        final ServletHolder restEasyServletHolder = new ServletHolder(new HttpServletDispatcher());
        restEasyServletHolder.setInitOrder(1);
        restEasyServletHolder.setInitParameter("javax.ws.rs.Application", MESSAGE_APPLICATION_NAME);
        restEasyServletHolder.setInitParameter("resteasy.servlet.mapping.prefix","/rest/services");
        restEasyServletHolder.setInitParameter("resteasy.document.expand.entity.references","false");
        restEasyServletHolder.setInitParameter("resteasy.role.based.security","true");
        context.addServlet(restEasyServletHolder, "/rest/services/*");

        final ServletHolder unsecuredRestEasyServletHolder = new ServletHolder(new HttpServletDispatcher());
        unsecuredRestEasyServletHolder.setInitOrder(1);
        unsecuredRestEasyServletHolder.setInitParameter("javax.ws.rs.Application", UNSECURED_MESSAGE_APPLICATION_NAME);
        unsecuredRestEasyServletHolder.setInitParameter("resteasy.servlet.mapping.prefix","/rest/unsecured");
        unsecuredRestEasyServletHolder.setInitParameter("resteasy.document.expand.entity.references","false");
        context.addServlet(unsecuredRestEasyServletHolder, "/rest/unsecured/*");
        
        ConstraintSecurityHandler constraintSecurityHandler = new ConstraintSecurityHandler();
        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        createSecurityConstraints(constraintMappings);
        constraintSecurityHandler.setConstraintMappings(constraintMappings);
        
        LoginService loginService = new UserLoginService("DRDevelopment users");

        FormAuthenticator authenticator = new FormAuthenticator("/login/index.html", "/login/index.html?error=x", false);
        constraintSecurityHandler.setAuthenticator(authenticator);
        constraintSecurityHandler.setLoginService(loginService);

        context.setSecurityHandler(constraintSecurityHandler);
    }
    
    public void addServlet(HttpServlet httpServlet, String contextPath) {
    	LOGGER.info("Registering servlet at context path http://localhost:{}{}", Config.getConfig().getWebserverHttpPort(), contextPath);
        context.addServlet(new ServletHolder(httpServlet), contextPath);
    }
    
	public void addAssetsServlet(String localResourceFolder, String contextPath, List<String> dynamicStaticPages) {
    	LOGGER.info("Registering assets servlet at context path http://localhost:{}{} for local resource folder {}", 
    			Config.getConfig().getWebserverHttpPort(), contextPath, localResourceFolder);
        context.addServlet(new ServletHolder(new AssetsServlet(localResourceFolder, dynamicStaticPages)), contextPath);
	}
	
	public void addAssetsServlet(String localResourceFolder, String contextPath, TemplateEngine templateEngine, 
			List<String> dynamicStaticPages) {
    	LOGGER.info("Registering assets servlet (with template engine) at context path http://localhost:{}{} for local resource folder {}", 
    			Config.getConfig().getWebserverHttpPort(), contextPath, localResourceFolder);
        context.addServlet(new ServletHolder(new AssetsServlet(localResourceFolder, templateEngine, dynamicStaticPages)), contextPath);
	}
	
	public void addRestServices(Set<Class<?>> restServices) {
		for (Class<?> restService : restServices) {
			LOGGER.info("Registering rest service from class {}", restService.getName());
		}
		SecureMessageApplication.addRestServices(restServices);
	}
	
	@Override
	public void run() {
		QueuedThreadPool threadPool = new QueuedThreadPool(1000, 10);
		threadPool.setName(WEB_THREAD);
		
		server = new Server(threadPool);
                
        HandlerCollection handlers = new HandlerCollection();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[] { requestLogHandler, context });

		StatisticsHandler stats = new StatisticsHandler();
        stats.setHandler(handlers);
        server.setHandler(stats);

        createRequestLogHandler(requestLogHandler);
        
        server.setStopAtShutdown(true);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);

        try {
        	setupConnectors(server);
        	installLifeCycleListener(server);
        
			server.start();
			server.join();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("Webserver stopped.");
	}

	private void createRequestLogHandler(RequestLogHandler requestLogHandler) {
        NCSARequestLog requestLog = new AsyncNCSARequestLog();
        requestLog.setFilename(Config.getConfig().getLogFolder() + File.separator + "access" + File.separator + "access-yyyy_mm_dd.log");
        requestLog.setExtended(true);
        requestLog.setRetainDays(3);
        requestLogHandler.setRequestLog(requestLog);
	}

	private void createSecurityConstraints(List<ConstraintMapping> constraintMappings) {
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[] { "user", "moderator", "admin", "superadmin" });
        constraint.setAuthenticate(true);

        Constraint relaxation = new Constraint();
        relaxation.setName(Constraint.ANY_ROLE);
        relaxation.setAuthenticate(false);

        ConstraintMapping relaxationMapping = new ConstraintMapping();
        relaxationMapping.setConstraint(relaxation);
        relaxationMapping.setPathSpec("/dashboard/app/*");

//        ConstraintMapping mapping1 = new ConstraintMapping();
//        mapping1.setPathSpec("/dashboard/app/*");
//        mapping1.setConstraint(constraint);
//        constraintMappings.add(mapping1);
        
        ConstraintMapping secureMapping = new ConstraintMapping();
        secureMapping.setPathSpec("/rest/services/*");
        secureMapping.setConstraint(constraint);

        ConstraintMapping secureMapping2 = new ConstraintMapping();
        secureMapping2.setPathSpec("/dashboard/app/*");
        secureMapping2.setConstraint(constraint);
        
        constraintMappings.add(relaxationMapping);
        constraintMappings.add(secureMapping);
        constraintMappings.add(secureMapping2);
	}

	public void start() {
		WebServer webserver = Services.getServices().getWebServer();
		if (webserver == null) {
			LOGGER.error("The webserver is not registered in the services and can not be started.");
		} else {
			Thread webserverThread = new Thread(webserver);
			webserverThread.setDaemon(true);
			webserverThread.start();
		}
	}
	
	public void stop() {
		try {
			if (server != null) {
				server.stop();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void setupConnectors(Server server) throws FileNotFoundException {
		boolean setupHttps = false;
		String keystoreFilename = Config.getConfig().getWebserverKeystoreFilename();
		if (keystoreFilename == null || keystoreFilename.isEmpty()) {
			LOGGER.info("No keystore file configured, so https is not setup.");
		} else {
			setupHttps = Config.getConfig().getWebserverHttpsPort() > 0;
			if (setupHttps) {
				LOGGER.info("Configure HTTPS on port {}", Config.getConfig().getWebserverHttpsPort());
			} else {
				LOGGER.info("No HTTPS configured.");
			}
		}
		
        // HTTP Configuration
        // HttpConfiguration is a collection of configuration information
        // appropriate for http and https. The default scheme for http is
        // <code>http</code> of course, as the default for secured http is
        // <code>https</code> but we show setting the scheme to show it can be
        // done. The port for secured communication is also set here.
        HttpConfiguration http_config = new HttpConfiguration();
        if (setupHttps) {
        	http_config.setSecureScheme("https");
        	http_config.setSecurePort(Config.getConfig().getWebserverHttpsPort());
        }
        http_config.setOutputBufferSize(32768);
        http_config.setSendServerVersion(true);
        
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(Config.getConfig().getWebserverHttpPort());
        http.setIdleTimeout(30000);
        
        if (setupHttps) {
        	SslContextFactory sslContextFactory = new SslContextFactory();
        	File keystoreFile = new File(Config.getConfig().getConfigurationFolder() + File.separator + keystoreFilename);
        	if (!keystoreFile.exists()) {
        		throw new FileNotFoundException(keystoreFile.getAbsolutePath());
        	}

        	sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
        	sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
        	sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");

        	HttpConfiguration https_config = new HttpConfiguration(http_config);
        	https_config.addCustomizer(new SecureRequestCustomizer());
        	//        https_config.addCustomizer(new ForwardedRequestCustomizer());

        	ServerConnector https = new ServerConnector(server,
        			new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
        			new HttpConnectionFactory(https_config));
        	https.setPort(Config.getConfig().getWebserverHttpsPort());
        	https.setIdleTimeout(500000);

        	server.setConnectors(new Connector[] { http, https });
        } else {
        	server.setConnectors(new Connector[] { http });
        }
	}
	
	private void installLifeCycleListener(Server server) {
        server.addLifeCycleListener(new Listener() {

			@Override
			public void lifeCycleStarting(LifeCycle event) {
			}
			
			@Override
			public void lifeCycleStarted(LifeCycle event) {
				HealthService.getHealthService();
				
				LOGGER.info("Started in {}ms.\nReady to rock 'n roll!\nGo to http(s)://localhost:{}/app or go to http(s)://localhost:{}/dashboard", 
						(System.currentTimeMillis() - Config.getConfig().getStartedOn()), 
						Config.getConfig().getWebserverHttpPort(), Config.getConfig().getWebserverHttpPort());
			}

			@Override
			public void lifeCycleFailure(LifeCycle event, Throwable cause) {
			}

			@Override
			public void lifeCycleStopping(LifeCycle event) {
			}

			@Override
			public void lifeCycleStopped(LifeCycle event) {
			}
		});
    }
}
