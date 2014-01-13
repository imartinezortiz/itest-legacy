package com.cesfelipesegundo.itis.biz.profiling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

public class ProfilerAspect implements Ordered {
	private static final Log log = LogFactory.getLog(ProfilerAspect.class);

	private int order = 1;
	
	private ProfilingStrategy profiler = new NoProfilingStrategy();

	public void setProfilingStrategy(ProfilingStrategy p) {
		this.profiler = p;
	}

	/**
	 * implementing the Ordered interface enables us to specify when this aspect
	 * should run with respect to other aspects such as transaction management.
	 * We give it the highest precedence (1)
	 */
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Object doProfile(ProceedingJoinPoint pjp) throws Throwable {
		Object token = this.profiler.start(pjp);
	    Object ret = pjp.proceed();
	    this.profiler.stop(token,pjp);
	    return ret;
	}
}
