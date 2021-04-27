package com.lm.masper.monitormaintainer.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

/**
 * A simple Enterprise Java Bean with a Mail Sender service
 * 
 * @author Luca M
 * @category EJB
 * @version 1.0.0
 * @see io.quarkus.mailer.Mailer Quarkus Mail Service
 *
 */
@ApplicationScoped
public class MailService {
	
	@Inject
	Logger log;
	
	@Inject
	Mailer mailer;
	
	@ConfigProperty(name = "monitor.mail.subject", defaultValue = "Monitoraggio e manutenzione")
	String subject;
	@ConfigProperty(name = "monitor.mail.addresses")
	Optional<String> optionalAddresses;
	
	private String htmlBody = null;
	
	
	public void sendMail(String htmlBody) {
		Set<String> addresses = getAddresses();
		this.htmlBody = htmlBody;
		
		if (addresses == null || addresses.size() == 0) {
			log.debug("No Email addresses, nothing to do");
			return;
		}
		
		log.debug("Sending email to " + addresses);
		addresses.forEach(this::createMailAndSend);
		
	}
	
	

	private void createMailAndSend(String emailAddress) {
		
		mailer.send(Mail.withHtml(
				emailAddress,
				this.subject, 
				htmlBody
				));
	}
	
	private Set<String> getAddresses() {
		log.debug("Splitting addresses: " + optionalAddresses.get());
		if (!optionalAddresses.isPresent())
			return null;
		final String fullAddresses = optionalAddresses.get();
		
		if (fullAddresses == null || fullAddresses.isEmpty())
			return null;
		
		if (!fullAddresses.contains(";"))
			return Set.of(fullAddresses);
		
		return new HashSet<>(Arrays.asList(fullAddresses.split(";")));
	}

	
	
	
}
