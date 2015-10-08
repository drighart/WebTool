package org.drdevelopment.webtool.plugin;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.drdevelopment.webtool.exception.PluginException;
import org.drdevelopment.webtool.plugin.api.PluginHealthService;
import org.drdevelopment.webtool.plugin.api.PluginMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.Extension;

@Extension
public class MailServiceExtension implements PluginMailService, PluginHealthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceExtension.class);

	@Override
	public void sendMail(String toAddress, String subject, String text, String htmlText) throws PluginException {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("midasbeursinfo@gmail.com", "midas2009"));
			email.setSSLOnConnect(true);
			email.setSSLCheckServerIdentity(true);
			email.setFrom("midasbeursinfo@gmail.com");
			email.addTo(toAddress);
			email.setSubject(subject);

			// embed the image and get the content id
			//		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
			//		String cid = email.embed(url, "Apache logo");

			email.setHtmlMsg(htmlText);
			email.setTextMsg(text);

			email.setDebug(true);
			email.send();
		} catch(Exception e) {
			throw new PluginException(e);
		}

	}

	@Override
	public Exception getStatus() {
		// Check email service if it is still available! If null is returned, no exception is returned, so it is available.
		return null;
	}

}
