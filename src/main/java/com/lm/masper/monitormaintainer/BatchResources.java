package com.lm.masper.monitormaintainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class BatchResources {
	
	@Inject
	Logger log;
	
	
	@Scheduled(every = "10s")
	public void scheduler() {
		log.debug("Procedure START");
		final long startTime = System.currentTimeMillis();
		try {
			
		} finally {
			log.debug("Procedure required [" + 
					(System.currentTimeMillis() + startTime) + "] millisec");
			log.debug("Procedure END");
		}
		
	}
	
	
}
