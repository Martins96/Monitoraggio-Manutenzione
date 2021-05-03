package com.lm.masper.monitormaintainer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.context.ApplicationContext;
import com.lm.masper.monitormaintainer.dto.PompaDTO;

import io.agroal.api.AgroalDataSource;

@ApplicationScoped
public class PompaDAO {
	
	private static final String QUERY_FOR_POMPACALDA = "SELECT * FROM pompacalda WHERE bar > ?";
	private static final String QUERY_FOR_POMPADOLCE = "SELECT * FROM pompadolce WHERE bar > ?";
	private static final String QUERY_FOR_POMPAPOZZO = "SELECT * FROM pompapozzo WHERE bar > ?";
	
	private static final String QUERY_FOR_POMPACALDA_WITH_DATE = "SELECT * FROM pompacalda "
			+ "WHERE bar > ? AND DataLettura > ?";
	private static final String QUERY_FOR_POMPADOLCE_WITH_DATE = "SELECT * FROM pompadolce "
			+ "WHERE bar > ? AND DataLettura > ?";
	private static final String QUERY_FOR_POMPAPOZZO_WITH_DATE = "SELECT * FROM pompapozzo "
			+ "WHERE bar > ? AND DataLettura > ?";
	
	
	private static final String QUERY_FOR_DATE_1 = "SELECT MAX(DataLettura) FROM pompacalda";
	private static final String QUERY_FOR_DATE_2 = "SELECT MAX(DataLettura) FROM pompadolce";
	private static final String QUERY_FOR_DATE_3 = "SELECT MAX(DataLettura) FROM pompapozzo";
	
	@Inject
	AgroalDataSource datasource;
	
	@Inject
	Logger log;
	
	@Inject
	ApplicationContext ctx;
	
	@ConfigProperty(name="max.bar.pompe", defaultValue = "100")
	int maxBar;
	
	
	@Retry(maxRetries = 3, delay = 250)
	public Map<String, List<PompaDTO>> getExeedsPressure() {
		final Map<String, List<PompaDTO>> results = new HashMap<>();
		final Timestamp alreadyFechedDate = ctx.getPumpLastWarnedDate();
		log.debug("Last date warned via mail was: " + alreadyFechedDate);
		
		try (final Connection conn = datasource.getConnection()) {
			ctx.setPumpLastWarnedDate(setFetchedDate(conn));
			
			final List<PompaDTO> pompaCaldaList = alreadyFechedDate == null ?
					executeQuery(QUERY_FOR_POMPACALDA, conn) :
					executeQuery(QUERY_FOR_POMPACALDA_WITH_DATE, conn, alreadyFechedDate);
			final List<PompaDTO> pompaDolceList = alreadyFechedDate == null ?
					executeQuery(QUERY_FOR_POMPADOLCE, conn) :
					executeQuery(QUERY_FOR_POMPADOLCE_WITH_DATE, conn, alreadyFechedDate);
			final List<PompaDTO> pompaPozzoList = alreadyFechedDate == null ?
					executeQuery(QUERY_FOR_POMPAPOZZO, conn) :
					executeQuery(QUERY_FOR_POMPAPOZZO_WITH_DATE, conn, alreadyFechedDate);
			
			results.put("pompacalda", pompaCaldaList);
			results.put("pompadolce", pompaDolceList);
			results.put("pompapozzo", pompaPozzoList);
		} catch (final SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
	private List<PompaDTO> executeQuery(String sql, Connection conn) {
		final List<PompaDTO> results = new ArrayList<PompaDTO>();
		try (final PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maxBar);
			try (final ResultSet rs = executeQuery(ps)) {
				if (rs != null) {
					while (rs.next()) {
						final PompaDTO dto = new PompaDTO();
						dto.setId(rs.getInt("id"));
						dto.setBar(rs.getInt("bar"));
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
	
	private List<PompaDTO> executeQuery(String sql, Connection conn, Timestamp date) {
		final List<PompaDTO> results = new ArrayList<PompaDTO>();
		try (final PreparedStatement ps = conn.prepareStatement(sql)) {
			log.debug("Quering with date: " + date);
			ps.setInt(1, maxBar);
			ps.setTimestamp(2, date);
			try (final ResultSet rs = executeQuery(ps)) {
				if (rs != null) {
					while (rs.next()) {
						final PompaDTO dto = new PompaDTO();
						dto.setId(rs.getInt("id"));
						dto.setBar(rs.getInt("bar"));
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
		try (final PreparedStatement ps1 = conn.prepareStatement(QUERY_FOR_DATE_1);
				final PreparedStatement ps2 = conn.prepareStatement(QUERY_FOR_DATE_2);
				final PreparedStatement ps3 = conn.prepareStatement(QUERY_FOR_DATE_3);
				final ResultSet rs1 = executeQuery(ps1);
				final ResultSet rs2 = executeQuery(ps2);
				final ResultSet rs3 = executeQuery(ps3);) {
			Timestamp maxDate1 = null;
			Timestamp maxDate2 = null;
			Timestamp maxDate3 = null;
			log.debug("Executed query time pompacalda: " + maxDate1);
			log.debug("Executed query time pompadolce: " + maxDate2);
			log.debug("Executed query time pompapozzo: " + maxDate3);
			if (rs1 != null && rs1.next()) {
				maxDate1 = rs1.getTimestamp(1);
			}
			if (rs2 != null && rs2.next()) {
				maxDate2 = rs2.getTimestamp(1);
			}
			if (rs3 != null && rs3.next()) {
				maxDate3 = rs3.getTimestamp(1);
			}
			
			Timestamp maxDate = maxDate1;
			if (maxDate2 != null && maxDate2.after(maxDate)) {
				maxDate = maxDate2;
			}
			if (maxDate3 != null && maxDate3.after(maxDate)) {
				maxDate = maxDate3;
			}
			
			log.debug("Max date from database for pump is " + maxDate);
			return maxDate;
			
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
			log.debug("| > Fields");
			final ResultSetMetaData rsmd = rs.getMetaData();
			final int column_count = rsmd.getColumnCount();
			for (int i = 1; i <= column_count; i++) {
				log.debug("| > - " + rsmd.getColumnName(i));
			}
			log.debug("---------------------------------------------------");
		}
		return rs;
	}
	
	
}
