package com.lm.masper.monitormaintainer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.context.ApplicationContext;
import com.lm.masper.monitormaintainer.dto.MacchinaDTO;

import io.agroal.api.AgroalDataSource;


@ApplicationScoped
public class MacchineDAO {
	
	private static final String QUERY_FOR_MACCHINA1 = 
			"SELECT * FROM macchina1 WHERE errore = 'errore'";
	private static final String QUERY_FOR_MACCHINA2 = 
			"SELECT * FROM macchina2 WHERE errore = 'errore'";
	
	private static final String QUERY_FOR_MACCHINA1_WITH_DATE = 
			"SELECT * FROM macchina1 WHERE errore = 'errore' AND DataLettura > ?";
	private static final String QUERY_FOR_MACCHINA2_WITH_DATE = 
			"SELECT * FROM macchina2 WHERE errore = 'errore' AND DataLettura > ?";
	
	private static final String QUERY_FOR_DATE = "SELECT GREATEST(MAX(m1.DataLettura), MAX(m2.DataLettura))"
			+ " FROM macchina1 AS m1, macchina2 AS m2";
	
	@Inject
	AgroalDataSource datasource;
	
	@Inject
	Logger log;
	
	@Inject
	ApplicationContext ctx;
	
	@Retry(maxRetries = 3, delay = 250)
	public Map<String, List<MacchinaDTO>> getMacchineOnError() {
		final Map<String, List<MacchinaDTO>> results = new HashMap<>();
		final Timestamp alreadyFechedDate = ctx.getMachineLastWarnedDate();
		log.debug("Last date warned via mail was: " + alreadyFechedDate);
		
		try (final Connection conn = datasource.getConnection()) {
			ctx.setMachineLastWarnedDate(setFetchedDate(conn));
			
			final List<MacchinaDTO> macchina1List = alreadyFechedDate == null ? 
					executeQuery(QUERY_FOR_MACCHINA1, conn) :
					executeQuery(QUERY_FOR_MACCHINA1_WITH_DATE, conn, alreadyFechedDate);
			final List<MacchinaDTO> macchina2List = alreadyFechedDate == null ? 
					executeQuery(QUERY_FOR_MACCHINA2, conn) :
					executeQuery(QUERY_FOR_MACCHINA2_WITH_DATE, conn, alreadyFechedDate);
			
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
				final ResultSet rs = executeQuery(ps)) {
			if (rs != null) {
				while (rs.next()) {
					final MacchinaDTO dto = new MacchinaDTO();
					dto.setId(rs.getInt("id"));
					dto.setErrore(rs.getString("errore"));
					dto.setDataLettura(rs.getDate("DataLettura"));
					results.add(dto);
				}
			}
			
		} catch (final SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
	private List<MacchinaDTO> executeQuery(String sql, Connection conn, Timestamp date) {
		final List<MacchinaDTO> results = new ArrayList<>();
		try (final PreparedStatement ps = conn.prepareStatement(sql)) {
			log.debug("Quering with date: " + date);
			ps.setTimestamp(1, date);
			try (final ResultSet rs = executeQuery(ps)) {
				if (rs != null) {
					while (rs.next()) {
						final MacchinaDTO dto = new MacchinaDTO();
						dto.setId(rs.getInt("id"));
						dto.setErrore(rs.getString("errore"));
						dto.setDataLettura(rs.getDate("DataLettura"));
						results.add(dto);
					}
				}
			}
			
		} catch (final SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
	
	private Timestamp setFetchedDate(final Connection conn) {
		try (final PreparedStatement ps = conn.prepareStatement(QUERY_FOR_DATE);
				final ResultSet rs = executeQuery(ps)) {
			if (rs != null && rs.next()) {
				final Timestamp maxDate = rs.getTimestamp(1);
				log.debug("Max date from database for machines is " + maxDate);
				return maxDate;
			}
			
		} catch (final SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		return null;
	}
	
	private ResultSet executeQuery(PreparedStatement ps) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("---------------------------------------------------");
			log.debug("| Executing query");
			log.debug("| " + ps.toString());
		}
		final ResultSet rs = ps.executeQuery();
		if (log.isDebugEnabled()) {
			log.debug("| Result");
			log.debug("| Fetch size: " + rs.getFetchSize());
			log.debug("---------------------------------------------------");
		}
		return rs;
	}
	
}
