package com.cesfelipesegundo.itis.biz.profiling;

import org.aspectj.lang.ProceedingJoinPoint;

public class NoProfilingStrategy implements ProfilingStrategy {

	public Object start(ProceedingJoinPoint jpStaticPart) {
		return null;
	}

	public void stop(Object token, ProceedingJoinPoint jpStaticPart) {
	}

}
