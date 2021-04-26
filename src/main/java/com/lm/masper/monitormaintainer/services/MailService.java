package com.lm.masper.monitormaintainer.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.util.ResourceUtils;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class MailService {
	
	@Inject
	Logger log;
	
	@Inject
	Mailer mailer;
	
	@ConfigProperty(name = "monitor.mail.subject", defaultValue = "Monitoraggio e manutenzione")
	String subject;
	
	String MAIL_BODY_CLASSPATH = "META-INF/mail/body.html";
	
	public void sendMail() {
		final Mail mail = createMail(""); // TODO add email addresses
		mailer.send(mail);
	}
	
	
	private Mail createMail(String emailAddress) {
		
		String editedBody = String.format(getBody());
		return Mail.withHtml(
				emailAddress,
				this.subject, 
				editedBody
				);
	}
	
	private String getBody() {
		try {
			return IOUtils.toString(ResourceUtils
					.getAsClasspathResource(MAIL_BODY_CLASSPATH), StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error("Error during read Mail body key ", e);
			return null;
		} finally {
			log.debug("Body mail read from config");
		}
	}
	
	
}
