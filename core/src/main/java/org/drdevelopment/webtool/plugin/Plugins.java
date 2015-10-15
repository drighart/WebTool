package org.drdevelopment.webtool.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.api.TemplateProcessor;
import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.exception.PluginNotFoundException;
import org.drdevelopment.webtool.exception.TechnicalException;
import org.drdevelopment.webtool.model.Plugin;
import org.drdevelopment.webtool.plugin.api.PluginAssetsServlet;
import org.drdevelopment.webtool.plugin.api.PluginDataupdate;
import org.drdevelopment.webtool.plugin.api.PluginHealthService;
import org.drdevelopment.webtool.plugin.api.PluginHttpServlet;
import org.drdevelopment.webtool.plugin.api.PluginInitServices;
import org.drdevelopment.webtool.plugin.api.PluginMailService;
import org.drdevelopment.webtool.plugin.api.PluginRestService;
import org.drdevelopment.webtool.plugin.api.PluginShutdownServices;
import org.drdevelopment.webtool.plugin.api.PluginUserService;
import org.drdevelopment.webtool.plugin.api.model.ConfigurationItem;
import org.drdevelopment.webtool.plugin.api.model.Image;
import org.drdevelopment.webtool.repository.ConfigurationItemRepository;
import org.drdevelopment.webtool.repository.DataVersionRepository;
import org.drdevelopment.webtool.repository.ImageRepository;
import org.drdevelopment.webtool.repository.PluginRepository;
import org.drdevelopment.webtool.util.FileUtil;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.PluginWrapper;

public class Plugins {
	private static final Logger LOGGER = LoggerFactory.getLogger(Plugins.class);
	private static Plugins pluginsClass;
	private LionPluginManager pluginManager;

	private Plugins() {
		pluginManager = new LionPluginManager();
	}

	public synchronized static Plugins getPlugins() {
		if (pluginsClass == null) {
			pluginsClass = new Plugins();
		}
		return pluginsClass;
	}

	public void load() throws SQLException {
		copyDefaultPlugInsInDevelopmentMode();
		
	    pluginManager.loadPlugins();
	    pluginManager.startPlugins();

        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper pluginWrapper : startedPlugins) {
            String pluginId = pluginWrapper.getDescriptor().getPluginId();

            LOGGER.info("Plugin: {}", pluginId);
            
            org.drdevelopment.webtool.model.Plugin plugin = PluginRepository.find(pluginId);
            if (plugin == null) {
            	PluginRepository.insert(pluginId, 0);
            }
            
            for (String extension : pluginManager.getExtensionClassNames(pluginId)) {
            	LOGGER.info("Extensions added by plugin {}: {}", pluginId, extension);
            }
        }

		setRunningPluginFlag(startedPlugins);
        
        // initiate plugins to attach services to the plugins which can be used.
        initPluginServices();
        
        // execute initial steps to make the plugins work.
        executeDataupdates();
        
        // check if no double user services are loaded. Else, exit the system.
        getUsersAuthenticationService();
        
        // register all http servlets which are defined in the plugins.
        registerHttpServlets();

        // register all assets servlets which are defined in the plugins.
        registerAssetServlets();
        
        // register all rest services which are defined in the plugins.
        registerRestServices();
	}

	public void setRunningPluginFlag(List<PluginWrapper> startedPlugins) {
		PluginRepository.setAllRunning(false);
        for (PluginWrapper pluginWrapper : startedPlugins) {
            String pluginId = pluginWrapper.getDescriptor().getPluginId();
            LOGGER.debug("Set running for plugin {}", pluginId);
    		PluginRepository.setRunning(pluginId, true);
        }
        
		List<Plugin> listPlugins = PluginRepository.find();
		for (Plugin plugin : listPlugins) {
			LOGGER.trace("status plugin: {}", plugin);
		}

	}

	private void initPluginServices() {
		PluginServices.getServices().setCoreServices(Services.getServices());
				
		List<LionExtensionWrapper<PluginInitServices>> pluginServices = pluginManager.getExtensionsAndDescriptorsByType(PluginInitServices.class);
		for (LionExtensionWrapper<PluginInitServices> wrapper : pluginServices) {
			
			PluginInitServices pluginService = wrapper.getExtension();
			LionExtensionDescriptor descr = wrapper.getDescriptor();

			pluginService.init(descr.getPluginId(), descr.getDescription(), descr.getPlugInClass(), descr.getVersion(), 
					Services.getServices().getDataSource());

			copyImagesFromPlugins(pluginService);
			
			registerTemplateProcessors(pluginService);
			
			updateConfigurationItems(pluginService, descr);
		}
	}

	public void copyImagesFromPlugins(PluginInitServices pluginService) {
		Set<Image> images = pluginService.getResourceImagesToCopy();
		if (images != null && !images.isEmpty()) {
			for (Image image : images) {
				byte[] data = image.getData();
				if (data == null || data.length == 0) {
					LOGGER.warn("No data to write for image with name {}", image.getName());
				} else {
					String imageFileName = Config.getConfig().getImagesFolder() + File.separator + image.getName();
					if (FileUtil.isFileExists(imageFileName)) {
						LOGGER.debug("Image file {} will not be overwritten.", imageFileName);
					} else {
						try {
							LOGGER.debug("Copy resource {} from plugin.", imageFileName);
							ImageRepository.writeImage(image.getName(), image.getTag(), data);
						} catch (IOException e) {
							LOGGER.warn(e.getMessage(), e);
						}
					}
				}
			}
		}
	}

	private void registerTemplateProcessors(PluginInitServices pluginService) {
		List<TemplateProcessor> templateProcessors = pluginService.getProcessors();
		if (templateProcessors != null && !templateProcessors.isEmpty()) {
			for (TemplateProcessor templateProcessor : templateProcessors) {
				LOGGER.debug("Registering template processor for tag {}.", 
						(templateProcessor.getTag() == null ? "" : templateProcessor.getTag()));
				Services.getServices().getTemplateEngine().register(templateProcessor);
			}
		}
	}

	private void updateConfigurationItems(PluginInitServices pluginService, LionExtensionDescriptor descr) {
		List<ConfigurationItem> configurationItems = pluginService.getConfigurationItems();
		if (configurationItems != null && !configurationItems.isEmpty()) {
			for (ConfigurationItem configurationItem : configurationItems) {
				LOGGER.debug("Registering configuration item with name {} and default value {}.", 
						configurationItem.getName(), displayConfigurationValue(configurationItem));
				org.drdevelopment.webtool.model.ConfigurationItem configurationItemFromDb = 
						ConfigurationItemRepository.findByName(descr.getPluginId(), configurationItem.getName());
				if (configurationItemFromDb == null) {
					ConfigurationItemRepository.insert(descr.getPluginId(), configurationItem.getKindOfInput(), 
							configurationItem.getName(), configurationItem.getDefaultValue());
//				} else {
//					ConfigurationItemRepository.update(descr.getPluginId(), configurationItem.getKindOfInput(),
//							configurationItem.getName(), configurationItem.getDefaultValue());
				}
			}
		}
	}

	private String displayConfigurationValue(ConfigurationItem configurationItem) {
		String defaultValue = configurationItem.getDefaultValue();
		String name = configurationItem.getName() == null ? "" : configurationItem.getName().toUpperCase().trim();
		if (name.contains("PASSWD") || name.contains("PASSWORD")) {
			defaultValue = "********";
		}
		return defaultValue;
	}
	
	public void shutdownPluginServices() {
		List<PluginShutdownServices> pluginShutdownServices = pluginManager.getExtensionsByType(PluginShutdownServices.class);
		for (PluginShutdownServices pluginShutdownService : pluginShutdownServices) {
			pluginShutdownService.shutdown();
		}
	}
	
	public PluginUserService getUsersAuthenticationService() throws TechnicalException {
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper pluginWrapper : startedPlugins) {
        	String pluginId = pluginWrapper.getDescriptor().getPluginId();

        	List<PluginUserService> userServices = pluginManager.getExtensionsByPlugin(PluginUserService.class, pluginId);
        	if (userServices != null && userServices.size() > 0) {
        		if (userServices.size() != 1) {
        			throw new TechnicalException("The plugin {} can only have one user authentication extension. It has now {} user authentication extensions.", 
        					pluginId, userServices.size());
        		} else {
        			return userServices.get(0);
        		}
        	}
        }
        throw new TechnicalException("No user authentication plugin found! The system should have one user authentication service. The interface {} should be implemented.", 
        		PluginUserService.class.getName());
	}

	public PluginMailService getMailService() throws PluginNotFoundException {
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper pluginWrapper : startedPlugins) {
        	String pluginId = pluginWrapper.getDescriptor().getPluginId();

        	List<PluginMailService> mailServices = pluginManager.getExtensionsByPlugin(PluginMailService.class, pluginId);
        	if (mailServices != null && mailServices.size() > 0) {
        		if (mailServices.size() != 1) {
        			throw new TechnicalException("The plugin {} can only have one mail service. It has now {} mail service extensions.", 
        					pluginId, mailServices.size());
        		} else {
        			return mailServices.get(0);
        		}
        	}
        }
        throw new PluginNotFoundException("No mail service available!");
	}

	public List<PluginHealthService> getHealthServices() {
		List<PluginHealthService> healthServices = new ArrayList<>();
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper pluginWrapper : startedPlugins) {
        	String pluginId = pluginWrapper.getDescriptor().getPluginId();

        	List<PluginHealthService> pluginHealthServices = pluginManager.getExtensionsByPlugin(PluginHealthService.class, pluginId);
        	healthServices.addAll(pluginHealthServices);
        }
        return healthServices;
	}
	
	private void executeDataupdates() throws SQLException {
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper pluginWrapper : startedPlugins) {
        	String pluginId = pluginWrapper.getDescriptor().getPluginId();

        	List<PluginDataupdate> dataUpdates = pluginManager.getExtensionsByPlugin(PluginDataupdate.class, pluginId);
        	if (dataUpdates != null && dataUpdates.size() > 0) {
        		if (dataUpdates.size() != 1) {
        			throw new TechnicalException("The plugin {} can only have one data update extension. It has now {} data update extensions.", 
        					pluginId, dataUpdates.size());
        		} else {
        			for (PluginDataupdate dataUpdate : dataUpdates) {
        				Integer version = DataVersionRepository.getPluginDataVersion(pluginId);
        				if (version == null) {
        					LOGGER.debug("No dataupdate record found for plugin {}. So, it will be created.", pluginId);
        					version = 0;
        					DataVersionRepository.pluginInsertNew(pluginId);
        				} 

        				int finalVersion = dataUpdate.getFinalDataupdateVersion();
        				while (version < finalVersion) {
        					LOGGER.debug("Retrieving SQL for plugin {} and version {} while final version is {}.", pluginId, version, finalVersion);
        					String sql = dataUpdate.getDataUpdate(version);
        					if (sql == null || sql.isEmpty()) {
        						LOGGER.debug("No SQL found to execute for dataupdate {} for plugin {}. The version will be increased.", version, pluginId);
        					} else {
        						LOGGER.debug("Executing SQL for plugin {}:\n{}", pluginId, sql);
        						RunScript.execute(Services.getServices().getDataSource().getConnection(), new StringReader(sql));
        						LOGGER.debug("Done running data update for plugin {} and version {}.", pluginId, version);
        					}
        					DataVersionRepository.pluginUpdate(pluginId, ++version);
        				}
        				
        				LOGGER.info("Executed data-update for plugin {}", pluginId);
        			}
        		}
        	}
        }
	}
	
	private void registerHttpServlets() {
	    List<PluginHttpServlet> servlets = pluginManager.getExtensionsByType(PluginHttpServlet.class);
	    for (PluginHttpServlet pluginHttpServlet : servlets) {
	    	javax.servlet.http.HttpServlet httpServlet = (javax.servlet.http.HttpServlet)pluginHttpServlet;
//	    	Services.getServices().getWebServer().addServlet(httpServlet, "/plugins/" + pluginHttpServlet.getContextPath() + "/*");
	    	Services.getServices().getWebServer().addServlet(httpServlet, pluginHttpServlet.getContextPath());
	    }
	}

	private void registerAssetServlets() {
	    List<PluginAssetsServlet> servlets = pluginManager.getExtensionsByType(PluginAssetsServlet.class);
	    for (PluginAssetsServlet pluginAssetsServlet : servlets) {
	    	if (Services.getServices().getTemplateEngine() != null) {
	    		Services.getServices().getWebServer().addAssetsServlet(pluginAssetsServlet.getResourcePath(), 
	    				pluginAssetsServlet.getContextPath(), Services.getServices().getTemplateEngine(), pluginAssetsServlet.getDynamicStaticPages());
	    	} else {
	    		Services.getServices().getWebServer().addAssetsServlet(pluginAssetsServlet.getResourcePath(), 
	    				pluginAssetsServlet.getContextPath(), pluginAssetsServlet.getDynamicStaticPages());
	    	}
	    }
	}

	private void registerRestServices() {
	    List<PluginRestService> pluginRestServices = pluginManager.getExtensionsByType(PluginRestService.class);
	    for (PluginRestService pluginRestService : pluginRestServices) {
	    		Services.getServices().getWebServer().addRestServices(pluginRestService.getRestServices());
	    }
	    LOGGER.info("To see all registered rest services, please check http://localhost:{}/rest/services/overview", 
	    		Config.getConfig().getWebserverHttpPort());
	}
	
    private static void copyDefaultPlugInsInDevelopmentMode() {
    	if (Config.getConfig().isDevelopmentMode()) {
    		LOGGER.debug("Copy default-plugins to the plugins folder, to make the software work (only in DEVELOPMENT_MODE).");
    		copyPlugIn("plugin-example", "drdevelopment-plugin-example-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-example-servlet", "drdevelopment-example-servlet-plugin-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-users", "drdevelopment-users-plugin-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-mailservice", "drdevelopment-mailservice-plugin-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-dashboard", "drdevelopment-dashboard-plugin-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-app-theme", "drdevelopment-app-theme-plugin-0.2-SNAPSHOT.zip");
    		copyPlugIn("plugin-setup-new-site", "drdevelopment-setup-new-site-plugin-0.2-SNAPSHOT.zip");
    	}
    }

    private static void copyPlugIn(String projectName, String nameOfJarFile) {
    	String fromFilename = Config.getConfig().getBaseFolder() + File.separator + ".." + File.separator + projectName + 
    			File.separator + "target" + File.separator + nameOfJarFile;
    	String toFilename = Config.getConfig().getPluginsFolder() + File.separator + nameOfJarFile;
    	if (FileUtil.isFileExists(fromFilename)) {
    		try {
    			LOGGER.debug("Copy file from {} to {}", fromFilename, toFilename);
				FileUtil.copyFile(fromFilename, toFilename);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
    	} else {
    		fromFilename = Config.getConfig().getBaseFolder() + File.separator + ".." + File.separator + ".." + File.separator + 
    				projectName + File.separator + "target" + File.separator + nameOfJarFile;
        	if (FileUtil.isFileExists(fromFilename)) {
        		try {
        			LOGGER.debug("Copy file from {} to {}", fromFilename, toFilename);
    				FileUtil.copyFile(fromFilename, toFilename);
    			} catch (IOException e) {
    				LOGGER.error(e.getMessage(), e);
    			}
        	}
    	}
    	
    }

	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		} else {
			LOGGER.warn("File {} already exists. It will be overwritten.", filename);
		}

		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}

}
