package com.lm.masper.monitormaintainer.dto;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Simple DTO for encapsulate Database data structure 
 * for tables PompaDolce PompaCalda e PompaPozzo
 * 
 * @author Luca M
 * @category Data Transport Object
 * @version 1.0.0
 * @see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">DTO</a>
 *
 */
public class PompaDTO implements Serializable {
	
	
	private static final long serialVersionUID = 7560435911829643288L;
	
	private int id;
	private int bar;
	private Date DataLettura;
	
	public PompaDTO() {
	}

	public PompaDTO(int id, int bar, Date dataLettura) {
		super();
		this.id = id;
		this.bar = bar;
		DataLettura = dataLettura;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBar() {
		return bar;
	}

	public void setBar(int bar) {
		this.bar = bar;
	}

	public Date getDataLettura() {
		return DataLettura;
	}

	public void setDataLettura(Date dataLettura) {
		DataLettura = dataLettura;
	}
	
	public String getDataLetturaFormatted() {
		return (DataLettura == null) ? "" : formatDate(DataLettura);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DataLettura == null) ? 0 : DataLettura.hashCode());
		result = prime * result + bar;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PompaDTO other = (PompaDTO) obj;
		if (DataLettura == null) {
			if (other.DataLettura != null)
				return false;
		} else if (!DataLettura.equals(other.DataLettura))
			return false;
		if (bar != other.bar)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PompaDTO [id=" + id + ", bar=" + bar + ", DataLettura=" +  formatDate(DataLettura) + "]";
	}
	
	public JsonObject toJson() {
		final JsonObjectBuilder bldr = Json.createObjectBuilder();
		bldr.add("id", id);
		bldr.add("bar", bar);
		if (DataLettura == null)
			bldr.add("DataLettura", JsonValue.NULL);
		else
			bldr.add("DataLettura", formatDate(DataLettura));
		return bldr.build();
	}
	
	private String formatDate(Date d) {
		if (d == null)
			return null;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
}
