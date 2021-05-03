package com.lm.masper.monitormaintainer.context;

import java.sql.Timestamp;

import javax.inject.Singleton;

@Singleton
public class ApplicationContext {
	
	// These dates are for exclude the already checked values on DB
	// With the date, the process will fetch only new values
	private Timestamp machineLastWarnedDate = null;
	private Timestamp pumpLastWarnedDate = null;
	
	
	public synchronized Timestamp getMachineLastWarnedDate() {
		return machineLastWarnedDate;
	}
	public synchronized void setMachineLastWarnedDate(Timestamp machineLastWarnedDate) {
		this.machineLastWarnedDate = machineLastWarnedDate;
	}
	public synchronized Timestamp getPumpLastWarnedDate() {
		return pumpLastWarnedDate;
	}
	public synchronized void setPumpLastWarnedDate(Timestamp pumpLastWarnedDate) {
		this.pumpLastWarnedDate = pumpLastWarnedDate;
	}
	
	
	
}
