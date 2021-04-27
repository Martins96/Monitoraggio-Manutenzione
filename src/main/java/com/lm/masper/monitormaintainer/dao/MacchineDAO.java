package com.lm.masper.monitormaintainer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.dto.MacchinaDTO;

import io.agroal.api.AgroalDataSource;


@ApplicationScoped
public class MacchineDAO {
	
	private static final String QUERY_FOR_MACCHINA1 = "SELECT * FROM macchina1 WHERE errore = 'errore'";
	private static final String QUERY_FOR_MACCHINA2 = "SELECT * FROM macchina2 WHERE errore = 'errore'";
	
	@Inject
	AgroalDataSource datasource;
	
	@Inject
	Logger log;
	
	@Retry(maxRetries = 3, delay = 250)
	public Map<String, List<MacchinaDTO>> getMacchineOnError() {
		final Map<String, List<MacchinaDTO>> results = new HashMap<>();
		try (final Connection conn = datasource.getConnection()) {
			final List<MacchinaDTO> macchina1List = executeQuery(QUERY_FOR_MACCHINA1, conn);
			final List<MacchinaDTO> macchina2List = executeQuery(QUERY_FOR_MACCHINA2, conn);
			results.put("macchina1", macchina1List);
			results.put("macchina2", macchina2List);
		} catch (SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
		
		
	}
	
	private List<MacchinaDTO> executeQuery(String sql, Connection conn) {
		final List<MacchinaDTO> results = new ArrayList<>();
		try (final PreparedStatement ps = conn.prepareStatement(sql);
				final ResultSet rs = ps.executeQuery();) {
			if (rs != null) {
				while (rs.next()) {
					final MacchinaDTO dto = new MacchinaDTO();
					dto.setId(rs.getInt("id"));
					dto.setErrore(rs.getString("errore"));
					dto.setDataLettura(rs.getDate("DataLettura"));
					results.add(dto);
				}
			}
			
		} catch (SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
}
