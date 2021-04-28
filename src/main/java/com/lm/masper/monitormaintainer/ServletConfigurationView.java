package com.lm.masper.monitormaintainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@Path("/config")
@ApplicationScoped
public class ServletConfigurationView {
	
	@Inject
	Logger log;
	
	@ConfigProperty(name = "monitor.mail.subject")
	String mailSub;
	@ConfigProperty(name = "monitor.mail.addresses")
	String mailAddresses;
	@ConfigProperty(name = "max.bar.pompe")
	int maxBarPompe;
	@ConfigProperty(name = "cron.expr.pompe")
	String cronTaskPompe;
	@ConfigProperty(name = "cron.expr.macchine")
	String cronTaskMacchine;
	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String databaseURL;
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String doGet() {
		final StringBuffer sb = new StringBuffer();
		//head
		sb.append("<html>\n<head>\n<title>");
		sb.append("Configurazioni applicative");
		sb.append("</title>\n");
		sb.append(getStyleTag());
		sb.append("</head>\n<body>\n");
		
		//body
		sb.append("<div class=\"title\"><h1>Messaggio di monitoraggio e manutenzione</h1></div>\n");
		sb.append("<p>Di seguito elencate le configurazioni attuali</p>\n");
		sb.append("<p>Per modificare le impostazioni seguire la guida "
				+ "<a href=\"https://github.com/Martins96/Monitoraggio-Manutenzione\">GitHub Monitoraggio-Manutenzione</a>"
				+ " e leggere il punto <b>Installer guide</b></p>\n");
		
		sb.append("<div class=\"mail-table\">\n");
		sb.append("<h3>Mail config</h3>\n");
		sb.append("<table><thead><tr><th>Chiave propriet&aacute;</th><th>Valore</th></tr></thead><tbody>\n");
		sb.append("<tr><th>" 
				+ "monitor.mail.subject" + "</th><th>" 
				+ mailSub + "</th></tr>");
		sb.append("<tr><th>" 
				+ "monitor.mail.addresses" + "</th><th>" 
				+ mailAddresses + "</th></tr>");
		sb.append("</tbody></table>\n");
		sb.append("</div>\n");
		
		sb.append("<div class=\"bar-table\">\n");
		sb.append("<h3>BAR config</h3>\n");
		sb.append("<table><thead><tr><th>Chiave propriet&aacute;</th><th>Valore</th></tr></thead><tbody>\n");
		sb.append("<tr><th>" 
				+ "max.bar.pompe" + "</th><th>" 
				+ maxBarPompe + "</th></tr>");
		sb.append("</tbody></table>\n");
		sb.append("</div>\n");
		
		sb.append("<div class=\"cron-table\">\n");
		sb.append("<h3>Cron task config</h3>\n");
		sb.append("<table><thead><tr><th>Chiave propriet&aacute;</th><th>Valore</th></tr></thead><tbody>\n");
		sb.append("<tr><th>" 
				+ "cron.expr.pompe" + "</th><th>" 
				+ cronTaskPompe + "</th></tr>");
		sb.append("<tr><th>" 
				+ "cron.expr.macchine" + "</th><th>" 
				+ cronTaskMacchine + "</th></tr>");
		sb.append("</tbody></table>\n");
		sb.append("</div>\n");
		
		sb.append("<div class=\"sb-table\">\n");
		sb.append("<h3>Database config</h3>\n");
		sb.append("<table><thead><tr><th>Chiave propriet&aacute;</th><th>Valore</th></tr></thead><tbody>\n");
		sb.append("<tr><th>" 
				+ "quarkus.datasource.jdbc.url" + "</th><th>" 
				+ databaseURL + "</th></tr>");
		sb.append("</tbody></table>\n");
		sb.append("</div>\n");
		
		
		sb.append("</body></html>");
		return sb.toString();
	}
	
	
	
	
	
	private String getStyleTag() {
		return "<style>"
				+ "body {font-family: Roboto,RobotoDraft,Helvetica,Arial,sans-serif;}\n"
				+ ".title {padding: 10px;background-color: #bbb;}\n"
				+ "table {border-collapse: collapse; width: 800px;}\n"
				+ "td, th {border: 1px solid #999; padding: 0.5rem; text-align: left;}\n"
				+ "tbody tr:nth-child(odd) {background-color: #ddd;}\n"
				+ "tbody th {font-weight: normal;}\n"
				+ "</style>\n";
	}
	
}
