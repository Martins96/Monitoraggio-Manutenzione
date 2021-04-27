package com.lm.masper.monitormaintainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.services.PompaEJB;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;

/**
 * This class define a batch trigger.<br/>
 * The schedule is defined via cron-like expression
 * 
 * @author Luca M
 * @category Batch
 * @see io.quarkus.scheduler.Scheduled Quarkus Scheduler Batch
 * @version 1.0.0
 *
 */
@ApplicationScoped
public class BatchResources {
	
	@Inject
	Logger log;
	
	@Inject
	PompaEJB pompaEJB;
	
	
	@Scheduled(cron = "{cron.expr.pompe}",  concurrentExecution = ConcurrentExecution.SKIP)
	public void scheduler() {
		log.debug("Procedure START");
		final long startTime = System.currentTimeMillis();
		try {
			pompaEJB.execution();
		} finally {
			log.debug("Procedure END");
			log.debug("Procedure required [" + (System.currentTimeMillis() - startTime) 
					+ "] millisec");
		}
		
	}
	
	
}
