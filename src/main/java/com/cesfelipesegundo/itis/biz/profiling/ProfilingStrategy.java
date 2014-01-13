package com.cesfelipesegundo.itis.biz.profiling;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ProfilingStrategy {
	public Object start(ProceedingJoinPoint jpStaticPart);

	public void stop(Object token, ProceedingJoinPoint jpStaticPart); 
}
