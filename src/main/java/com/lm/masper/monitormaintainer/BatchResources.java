package com.lm.masper.monitormaintainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.lm.masper.monitormaintainer.services.MacchineEJB;
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
	
	@Inject
	MacchineEJB macchineEJB;
	
	
	@Scheduled(cron = "{cron.expr.pompe}",  concurrentExecution = ConcurrentExecution.SKIP)
	public void schedulerPompe() {
		log.debug("Procedure POMPE START");
		final long startTime = System.currentTimeMillis();
		try {
			pompaEJB.execution();
		} finally {
			log.debug("Procedure POMPE END");
			log.info("Procedure POMPE required [" + (System.currentTimeMillis() - startTime) 
					+ "] millisec");
		}
		
	}
	
	
	@Scheduled(cron = "{cron.expr.macchine}",  concurrentExecution = ConcurrentExecution.SKIP)
	public void schedulerMacchine() {
		log.debug("Procedure MACCHINE START");
		final long startTime = System.currentTimeMillis();
		try {
			macchineEJB.execution();
		} finally {
			log.debug("Procedure MACCHINE END");
			log.info("Procedure MACCHINE required [" + (System.currentTimeMillis() - startTime) 
					+ "] millisec");
		}
		
	}
	
	
}
