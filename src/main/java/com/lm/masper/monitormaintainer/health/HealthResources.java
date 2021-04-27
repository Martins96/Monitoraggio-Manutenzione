package com.lm.masper.monitormaintainer.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

/**
 * Expose health APIs in order to check the application availability
 * 
 * @author Luca M
 * @category health
 * @version 1.0.0
 * @see org.eclipse.microprofile.health.HealthCheck Microprofile HealthCheck
 *
 */
@Readiness
@Liveness
public class HealthResources implements HealthCheck {
	
	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.up("general");
	}
	
}
