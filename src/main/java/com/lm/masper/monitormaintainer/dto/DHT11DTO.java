package com.lm.masper.monitormaintainer.dto;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Simple DTO for encapsulate Database data structure 
 * for tables dht11
 * 
 * @author Luca M
 * @category Data Transport Object
 * @version 1.0.0
 * @see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">DTO</a>
 *
 */
public class DHT11DTO {
	
	private int id;
	private int bar;
	private Date date;

	public DHT11DTO() {
	}

	public DHT11DTO(int id, int bar, Date date) {
		super();
		this.id = id;
		this.bar = bar;
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bar;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		DHT11DTO other = (DHT11DTO) obj;
		if (bar != other.bar)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DHT11DTO [id=" + id + ", bar=" + bar + ", date=" + formatDate(date) + "]";
	}
	
	public JsonObject toJson() {
		final JsonObjectBuilder bldr = Json.createObjectBuilder();
		bldr.add("id", id);
		bldr.add("bar", bar);
		if (date == null)
			bldr.add("date", JsonValue.NULL);
		else
			bldr.add("date", formatDate(date));
		return bldr.build();
	}
	
	private String formatDate(Date d) {
		if (d == null)
			return null;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
	
}
