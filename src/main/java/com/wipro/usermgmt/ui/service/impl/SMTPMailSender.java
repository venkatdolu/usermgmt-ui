package com.wipro.usermgmt.ui.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wipro.usermgmt.ui.model.User;
import com.wipro.usermgmt.ui.service.MailSender;


/**
 * @author Venkat
 * 
 * Description: send mails using smtp server.
 *
 */
@Service
public class SMTPMailSender implements MailSender {
	
	private static final Logger Log = LoggerFactory.getLogger(SMTPMailSender.class);
	
	@Value("${usrmgmt.smtp.mail.host}")
	private String SMTP_HOST;
	
	@Value("${usrmgmt.smtp.mail.port}")
	private String SMTP_PORT;
	
	@Value("${usrmgmt.smtp.mail.auth}")
	private String SMTP_AUTH;
	
	@Value("${usrmgmt.smtp.mail.ttls.enable}")
	private String SMTP_TTLS_ENABLE;
	
	@Value("${usrmgmt.smtp.mail.user.name}")
	private String userName;
	
	@Value("${usrmgmt.smtp.mail.password}")
	private String password;
	

	/**
	 * Description : used to send verficationmail to activate the user
	 */
	@Override
	public void sendVerificationEmail(User user, String siteURL) {

		Log.info("sendVerificationEmail method invoked for username {} .", user.getUserName());

		String subject = "Please verify your registration";
		String content = "Dear [[name]] <p>" + "Please click the link below to verify your registration:<p>"
				+ "<h2><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h2> <br/><br/>" + "Thank you,<br/>";

		try {

			Properties prop = new Properties();
			prop.put("mail.smtp.host", SMTP_HOST);
			prop.put("mail.smtp.port", SMTP_PORT);
			prop.put("mail.smtp.auth", SMTP_AUTH);
			prop.put("mail.smtp.starttls.enable", SMTP_TTLS_ENABLE); // TLS

			Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});

			content = content.replace("[[name]]", user.getFullName());
			String verifyURL = siteURL + "/verify?code=" + user.getPassword() + "&id=" + user.getId();

			content = content.replace("[[URL]]", verifyURL);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
			message.setSubject(subject);
			message.setContent(content, "text/html");
			new Thread(() -> {
				try {
					Log.info("Sending verification email to: {} for username {}.", user.getEmail(), user.getUserName());
					Transport.send(message);
					Log.info("sent verification email to: {} for username {}.", user.getEmail(), user.getUserName());
				} catch (MessagingException e) {
					Log.warn("Error occured while sending mail to username {}. Reason: {} .", user.getUserName(),
							e.getMessage());
					throw new RuntimeException("Error occured while sending mail.", e);
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException("Error occured while sending mail.", e);
		}

	}

}
