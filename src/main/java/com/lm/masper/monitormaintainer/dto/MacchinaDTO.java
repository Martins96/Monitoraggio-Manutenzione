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
 * for tables Macchina1 and Macchina2
 * 
 * @author Luca M
 * @category Data Transport Object
 * @version 1.0.0
 * @see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">DTO</a>
 *
 */
public class MacchinaDTO implements Serializable {
	
	private static final long serialVersionUID = 1983806884448102460L;
	
	
	private int id;
	private String errore;
	private Date dataLettura;
	
	public MacchinaDTO() {
	}

	public MacchinaDTO(int id, String errore, Date dataLettura) {
		super();
		this.id = id;
		this.errore = errore;
		this.dataLettura = dataLettura;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getErrore() {
		return errore;
	}
	public void setErrore(String errore) {
		this.errore = errore;
	}

	public Date getDataLettura() {
		return dataLettura;
	}
	public void setDataLettura(Date dataLettura) {
		this.dataLettura = dataLettura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataLettura == null) ? 0 : dataLettura.hashCode());
		result = prime * result + ((errore == null) ? 0 : errore.hashCode());
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
		MacchinaDTO other = (MacchinaDTO) obj;
		if (dataLettura == null) {
			if (other.dataLettura != null)
				return false;
		} else if (!dataLettura.equals(other.dataLettura))
			return false;
		if (errore == null) {
			if (other.errore != null)
				return false;
		} else if (!errore.equals(other.errore))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MacchinaDTO [id=" + id + ", errore=" + errore + ", dataLettura=" + dataLettura + "]";
	}
	
	public JsonObject toJson() {
		final JsonObjectBuilder bldr = Json.createObjectBuilder();
		bldr.add("id", id);
		bldr.add("errore", errore);
		if (dataLettura == null)
			bldr.add("DataLettura", JsonValue.NULL);
		else
			bldr.add("DataLettura", formatDate(dataLettura));
		return bldr.build();
	}
	
	private String formatDate(Date d) {
		if (d == null)
			return null;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
}
