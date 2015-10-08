package org.drdevelopment.webtool.mail;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

public class TestSendMail {

//	@Test
//	public void testSendMail2() {
//		Email email = new Email.Builder()
//	    .from("midas", "midasbeursinfo@gmail.com")
//	    .to("David", "david.righart@gmail.com")
//	    .subject("hey")
//	    .text("We should meet up! ;)")
//	    .build();
//		
//		new Mailer("smtp.gmail.com", 465, "midasbeursinfo@gmail.com", "midas2009", TransportStrategy.SMTP_SSL).sendMail(email);
////		new Mailer("smtp.gmail.com", 587, "midasbeursinfo@gmail.com", "midas2009", TransportStrategy.SMTP_TLS).sendMail(email);
//	}
	
	@Test
	public void testSendMail() throws AddressException, MessagingException, GeneralSecurityException {
        Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", 465);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", 465);
		props.put("mail.smtp.ssl.enable", true);
		
		Session session = Session.getInstance(props, null);
        session.setDebug(true);
        
        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress("midasbeursinfo@gmail.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("david.righart@gmail.com", false));
        msg.setSubject("Test");
        msg.setText("Test", "utf-8");
        msg.setSentDate(new Date());
        
        Transport transport = session.getTransport("smtps");
        transport.connect("smtp.gmail.com", 465, "midasbeursinfo@gmail.com", "midas2009");
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
	}

	@Test
	public void sendApacheMail() throws EmailException, MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("midasbeursinfo@gmail.com", "midas2009"));
		email.setSSLOnConnect(true);
		email.setSSLCheckServerIdentity(true);
		email.setFrom("midasbeursinfo@gmail.com");
		email.addTo("david.righart@gmail.com");
		email.setSubject("TestMail");

		// embed the image and get the content id
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid = email.embed(url, "Apache logo");

		// set the html message
		email.setHtmlMsg("<html>The apache logo - <img src=\"cid:"+cid+"\"><br/><br/><br/><b>Hellowa</b> big World!!!"
				+ "<br><br><a href=\"http://www.w3schools.com/html/\">Visit our HTML tutorial</a></html>");

		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		email.setDebug(true);
		email.send();
	}
}
