package com.cesfelipesegundo.itis.biz.profiling;

import org.aspectj.lang.ProceedingJoinPoint;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class JamonProfilingStrategy implements ProfilingStrategy {

	public void reset(){
		MonitorFactory.reset();
	}
	
	public String report(){
		return MonitorFactory.getReport();
	}
	
	public Object start(ProceedingJoinPoint jpStaticPart) {
		return MonitorFactory.start(jpStaticPart.toShortString());
	}

	public void stop(Object token, ProceedingJoinPoint jpStaticPart) {
		if (token instanceof Monitor) {
			Monitor mon = (Monitor) token;
			mon.stop();
		}
	}

}
