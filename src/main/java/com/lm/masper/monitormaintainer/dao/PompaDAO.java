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

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.dto.PompaDTO;

import io.agroal.api.AgroalDataSource;

@ApplicationScoped
public class PompaDAO {
	
	private static final String QUERY_FOR_POMPACALDA = "SELECT * FROM pompacalda WHERE bar > ?";
	private static final String QUERY_FOR_POMPADOLCE = "SELECT * FROM pompadolce WHERE bar > ?";
	private static final String QUERY_FOR_POMPAPOZZO = "SELECT * FROM pompapozzo WHERE bar > ?";
	
	@Inject
	AgroalDataSource datasource;
	
	@Inject
	Logger log;
	
	@ConfigProperty(name="max.bar.pompe", defaultValue = "100")
	int maxBar;
	
	
	@Retry(maxRetries = 3, delay = 250)
	public Map<String, List<PompaDTO>> getExeedsPressure() {
		final Map<String, List<PompaDTO>> results = new HashMap<>();
		try (final Connection conn = datasource.getConnection()) {
			final List<PompaDTO> pompaCaldaList = executeQuery(QUERY_FOR_POMPACALDA, conn);
			final List<PompaDTO> pompaDolceList = executeQuery(QUERY_FOR_POMPADOLCE, conn);
			final List<PompaDTO> pompaPozzoList = executeQuery(QUERY_FOR_POMPAPOZZO, conn);
			results.put("pompacalda", pompaCaldaList);
			results.put("pompadolce", pompaDolceList);
			results.put("pompapozzo", pompaPozzoList);
		} catch (SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
	private List<PompaDTO> executeQuery(String sql, Connection conn) {
		final List<PompaDTO> results = new ArrayList<PompaDTO>();
		try (final PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maxBar);
			try (final ResultSet rs = ps.executeQuery()) {
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
			
		} catch (SQLException e) {
			log.error("Error connecting DB: ", e);
		}
		
		return results;
	}
	
	
}
