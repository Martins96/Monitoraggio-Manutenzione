package com.lm.masper.monitormaintainer.services;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.dao.MacchineDAO;
import com.lm.masper.monitormaintainer.dto.MacchinaDTO;
import com.lm.masper.monitormaintainer.services.mail.MailBodyGeneratorForMacchine;

/**
 * Enterprise Java Bean for logic on machines (1 & 2)
 * 
 * @author Luca M
 * @category EJB
 * @version 1.0.0
 *
 */
@ApplicationScoped
public class MacchineEJB {
	
	@Inject
	Logger log;
	
	@Inject
	MacchineDAO macchineDAO;
	
	@Inject
	MailService mailer;
	
	public void execution() {
		final Map<String, List<MacchinaDTO>> dbResults = macchineDAO.getMacchineOnError();
		if (dbResults == null || dbResults.size() == 0) {
			log.error("Ooops, somethings wrong on DB");
			return;
		}
		
		final List<MacchinaDTO> macchina1List = dbResults.get("macchina1");
		final List<MacchinaDTO> macchina2List = dbResults.get("macchina2");
		if (log.isDebugEnabled()) {
			log.debug("DB call completed");
			log.debug("macchina1: " + macchina1List);
			log.debug("macchina2: " + macchina2List);
		}
		
		final StringBuffer macchine1Table = generateHTMLTable(macchina1List),
				macchine2Table = generateHTMLTable(macchina2List);
		
		if (macchine1Table == null && macchine2Table == null) {
			log.debug("All machines are in order, nothing to do");
			return;
		}
		
		log.debug("Ready to advise via mail");
		final String mailBody = new MailBodyGeneratorForMacchine(macchine1Table, macchine2Table)
				.getFormattedHTML();
		
		mailer.sendMail(mailBody);
		
		
	}
	
	
	private StringBuffer generateHTMLTable(final List<MacchinaDTO> macchinaList) {
		if (macchinaList == null || macchinaList.size() == 0) {
			log.debug("Macchina query size is 0");
			return null;
		}
		final StringBuffer sb = new StringBuffer();
		
		macchinaList.forEach(dto -> {
			sb.append(tableEntry(dto));
		});
		
		return sb;
	}
	
	private String tableEntry(MacchinaDTO dto) {
		if (dto == null)
			return "";
		final StringBuffer sb = new StringBuffer("<tr>");
		sb.append("<td>" + dto.getId() + "</td>");
		sb.append("<td>" + dto.getErrore() + "</td>");
		sb.append("<td>" + dto.getDataLettura() + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	
}
