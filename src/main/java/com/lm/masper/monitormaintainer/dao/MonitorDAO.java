package com.lm.masper.monitormaintainer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import io.agroal.api.AgroalDataSource;

@ApplicationScoped
public class MonitorDAO {
	
	private static final String QUERY_FOR_DATA = "SELECT * FROM tmp_table";
	
	@Inject
	AgroalDataSource datasource;
	
	@Inject
	Logger log;
	
	
	@Retry(maxRetries = 3, delay = 250)
	public String getData() {
		final StringBuffer sb = new StringBuffer();
		try (final Connection conn = datasource.getConnection();
				final PreparedStatement ps = conn.prepareStatement(QUERY_FOR_DATA);
				final ResultSet rs = ps.executeQuery();) {
			if (rs != null) {
				while (rs.next()) {
					sb.append(rs.getString("TMP_COL1") + ":");
					sb.append(rs.getString("TMP_COL2"));
					sb.append(";");
				}
			}
			
		} catch (SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return sb.toString();
	}
	
	
}
