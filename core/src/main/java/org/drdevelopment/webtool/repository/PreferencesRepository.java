package org.drdevelopment.webtool.repository;

import org.drdevelopment.webtool.Services;
import org.drdevelopment.webtool.dao.ConfigurationItemDao;
import org.drdevelopment.webtool.model.ConfigurationItem;
import org.drdevelopment.webtool.model.Preferences;

public class PreferencesRepository {

	private PreferencesRepository() {
		// no constructor.
	}
	
	public static Preferences getPreferences() {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
		Preferences preferences = new Preferences();
		preferences.setWebsiteImageName(getValue(dao, "Website image-name"));
		preferences.setContactEmail(getValue(dao, "Contact Email"));
		preferences.setContactPhone(getValue(dao, "Contact Phone"));
		preferences.setWebsiteName(getValue(dao, "Website name"));
		preferences.setWebsiteTitle(getValue(dao, "Website title"));
		preferences.setWebsiteSubTitle(getValue(dao, "Website sub-title"));
		
//		'Website primary color', '#f05f40', 'app-theme-plugin', 'COLOR'),
//		'Website button press color', '#EE4B28', 'app-theme-plugin', 'COLOR')
//		'Website border color', '#ed431f', 'app-theme-plugin', 'COLOR'),
//		'Website header color', '#fff', 'app-theme-plugin', 'COLOR'),
//		'Website text faded color', 'rgba(255,255,255,.7)', 'app-theme-plugin
//		'Website header image', 'header.jpg', 'app-theme-plugin', 'TEXT');   

		
        Services.getServices().getDbi().close(dao);
        return preferences;
	}

	private static String getValue(ConfigurationItemDao dao, String name) {
		ConfigurationItem item = dao.find(name);
		return (item != null && item.getValue() != null ? item.getValue() : "");
	}
	
	public static void update(Preferences preferences) {
		ConfigurationItemDao dao = Services.getServices().getDbi().open(ConfigurationItemDao.class);
		update(dao, "Website image-name", preferences.getWebsiteImageName());
		update(dao, "Contact Email", preferences.getContactEmail());
		update(dao, "Contact Phone", preferences.getContactPhone());
		update(dao, "Website name", preferences.getWebsiteName());
		update(dao, "Website title", preferences.getWebsiteTitle());
		update(dao, "Website sub-title", preferences.getWebsiteSubTitle());
		Services.getServices().getDbi().close(dao);
	}

	private static void update(ConfigurationItemDao dao, String name, String value) {
		ConfigurationItem item = dao.find(name);
		if (item == null) {
			dao.insert("", "", name, value);
		} else {
			dao.save(name, value);
		}
	}
}
