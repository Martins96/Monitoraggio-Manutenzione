package com.lm.masper.monitormaintainer.services;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.dao.PompaDAO;
import com.lm.masper.monitormaintainer.dto.PompaDTO;
import com.lm.masper.monitormaintainer.services.mail.MailBodyGeneratorForPompe;

@ApplicationScoped
public class PompaEJB {
	
	@Inject
	Logger log;
	
	@Inject
	PompaDAO pompaDAO;
	
	@Inject
	MailService mailer;
	
	public void execution() {
		final Map<String, List<PompaDTO>> dbResults = pompaDAO.getExeedsPressure();
		if (dbResults == null || dbResults.size() == 0) {
			log.error("Ooops, somethings wrong on DB");
			return;
		}
		
		final List<PompaDTO> pompacaldaList = dbResults.get("pompacalda");
		final List<PompaDTO> pompadolceList = dbResults.get("pompadolce");
		final List<PompaDTO> pompapozzoList = dbResults.get("pompapozzo");
		if (log.isDebugEnabled()) {
			log.debug("DB call completed");
			log.debug("pompacalda: " + pompacaldaList);
			log.debug("pompadolce: " + pompadolceList);
			log.debug("pompapozzo: " + pompapozzoList);
		}
		
		final StringBuffer pompaCaldaTable = generateHTMLTable(pompacaldaList),
				pompaDolceTable = generateHTMLTable(pompadolceList), 
				pompaPozzoTable = generateHTMLTable(pompapozzoList);
		
		if (pompaCaldaTable == null && pompaDolceTable == null && pompaPozzoTable == null) {
			log.debug("All pumps are in order, nothing to do");
			return;
		}
		
		log.debug("Ready to advise via mail");
		final String mailBody = new MailBodyGeneratorForPompe(pompaCaldaTable, pompaDolceTable, pompaPozzoTable)
				.getFormattedHTML();
		
		mailer.sendMail(mailBody);
		
		
	}
	
	
	private StringBuffer generateHTMLTable(final List<PompaDTO> pompaList) {
		if (pompaList == null || pompaList.size() == 0) {
			log.debug("Pompa query size is 0");
			return null;
		}
		final StringBuffer sb = new StringBuffer();
		
		pompaList.forEach(dto -> {
			sb.append(tableEntry(dto));
		});
		
		return sb;
	}
	
	private String tableEntry(PompaDTO dto) {
		if (dto == null)
			return "";
		final StringBuffer sb = new StringBuffer("<tr>");
		sb.append("<td>" + dto.getId() + "</td>");
		sb.append("<td>" + dto.getBar() + "</td>");
		sb.append("<td>" + dto.getDataLettura() + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	
}
