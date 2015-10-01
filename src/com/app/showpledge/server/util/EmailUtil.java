package com.app.showpledge.server.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility that sends emails.
 * 
 * @author mjdowell
 *
 */
public class EmailUtil {

	//private static final String EMAIL_FROM = "no_reply@showpledge.com";
	private static final String EMAIL_FROM = "admin@showpledge.com";
	private static final String EMAIL_FROM_PERSONAL = "Showpledge.com Admin";

	public static void send(String inTo, String inToName, String inSubj, String inBody) throws Exception {

		send(inTo, inToName, EMAIL_FROM, EMAIL_FROM_PERSONAL, inSubj, inBody);

	}
	
	public static void send(String inTo, String inToName, String inFromEmail, String inFromName, String inSubj, String inBody) throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(inFromEmail, inFromName));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(inTo, inToName));
		msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(EMAIL_FROM, EMAIL_FROM_PERSONAL));
		msg.setSubject(inSubj);
		msg.setText(inBody);
		Transport.send(msg);

	}	

}
