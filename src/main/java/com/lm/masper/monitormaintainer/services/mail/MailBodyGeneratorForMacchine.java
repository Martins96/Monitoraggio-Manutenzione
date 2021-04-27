package com.lm.masper.monitormaintainer.services.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.services.MailService;
import com.lm.masper.monitormaintainer.util.ResourceUtils;

import static com.lm.masper.monitormaintainer.services.mail.MailConstants.*;

/**
 * This class generate the HTML Body for report mail
 * 
 * @author Luca M
 * @category Mail Logic
 * @version 1.0.0
 * @see MailService
 *
 */
public class MailBodyGeneratorForMacchine {
	
	private final String MAIL_BODY_CLASSPATH = "META-INF/mail/body-macchine.html";
	private final Logger log;
	
	private final String macchina1Table, macchina2Table;
	
	
	public MailBodyGeneratorForMacchine(StringBuffer macchina1Table, 
			StringBuffer macchina2Table) {
		log = Logger.getLogger(getClass().getCanonicalName());
		this.macchina1Table = macchina1Table == null ? "" : macchina1Table.toString();
		this.macchina2Table = macchina2Table == null ? "" : macchina2Table.toString();
	}
	
	
	public String getFormattedHTML() {
		String tmpBody = getBody();
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_CURRENT_DATE, 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:SS:sss").format(new Date()));
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_MACCHINA1_TITLE, "Macchina 1");
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_MACCHINA1_DATA, macchina1Table);
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_MACCHINA2_TITLE, "Macchina 2");
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_MACCHINA2_DATA, macchina2Table);
		
		return tmpBody;
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
