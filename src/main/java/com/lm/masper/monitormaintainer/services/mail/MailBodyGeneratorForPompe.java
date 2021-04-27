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
public class MailBodyGeneratorForPompe {
	
	private final String MAIL_BODY_CLASSPATH = "META-INF/mail/body-pompe.html";
	private final Logger log;
	
	private final String pompaCaldaTable, pompaDolceTable, pompaPozzoTable;
	
	
	public MailBodyGeneratorForPompe(StringBuffer pompaCaldaTable, 
			StringBuffer pompaDolceTable, StringBuffer pompaPozzoTable) {
		log = Logger.getLogger(getClass().getCanonicalName());
		this.pompaCaldaTable = pompaCaldaTable == null ? "" : pompaCaldaTable.toString();
		this.pompaDolceTable = pompaDolceTable == null ? "" : pompaDolceTable.toString();
		this.pompaPozzoTable = pompaPozzoTable == null ? "" : pompaPozzoTable.toString();
	}
	
	
	public String getFormattedHTML() {
		String tmpBody = getBody();
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_CURRENT_DATE, 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:SS:sss").format(new Date()));
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_CALDA_TITLE, "Pompa Calda");
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_CALDA_DATA, pompaCaldaTable);
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_DOLCE_TITLE, "Pompa Dolce");
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_DOLCE_DATA, pompaDolceTable);
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_POZZO_TITLE, "Pompa Pozzo");
		tmpBody = tmpBody.replace(BODY_PLACEHOLDER_TABLE_POZZO_DATA, pompaPozzoTable);
		
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
