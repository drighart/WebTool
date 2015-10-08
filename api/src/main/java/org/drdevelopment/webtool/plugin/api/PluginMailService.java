package org.drdevelopment.webtool.plugin.api;

import org.drdevelopment.webtool.exception.PluginException;

import ro.fortsoft.pf4j.ExtensionPoint;


public interface PluginMailService extends ExtensionPoint {

	void sendMail(String toAddress, String subject, String text, String htmlText) throws PluginException;
}
