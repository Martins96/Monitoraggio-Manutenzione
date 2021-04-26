package com.lm.masper.monitormaintainer.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@Liveness
public class HealthResources implements HealthCheck {
	
	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.up("general");
	}
	
}
