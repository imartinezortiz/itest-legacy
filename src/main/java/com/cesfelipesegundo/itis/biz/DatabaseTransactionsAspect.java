package com.cesfelipesegundo.itis.biz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

public class DatabaseTransactionsAspect implements Ordered {
	private static final Log log = LogFactory.getLog(DatabaseTransactionsAspect.class);
	
	private static final int DEFAULT_MAX_RETRIES = 2;
	private int maxRetries = DEFAULT_MAX_RETRIES;
	private int order = 1;
	private boolean retryOnOptimisticLockFailure = false;

	/**
	 * configurable number of retries
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/**
	 * Whether or not optimistic lock failures should also be retried. Default
	 * is not to retry transactions that fail due to optimistic locking in case
	 * we overwrite another user's work.
	 */
	public void setRetryOnOptimisticLockFailure(boolean retry) {
		this.retryOnOptimisticLockFailure = retry;
	}

	/**
	 * implementing the Ordered interface enables us to specify when this aspect
	 * should run with respect to other aspects such as transaction management.
	 * We give it the highest precedence (1) which means that the retry logic
	 * will wrap the transaction logic - we need a fresh transaction each time.
	 */
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Object doDatabaseOperation(ProceedingJoinPoint pjp)
			throws Throwable {
		int numAttempts = 0;
		DataAccessException failureException;
		do {
			try {
				return pjp.proceed();
			} catch (OptimisticLockingFailureException ex) {
				log.info("Error running transaction", ex);
				if (!this.retryOnOptimisticLockFailure) {
					throw ex;
				} else {
					failureException = ex;
				}
			} catch (ConcurrencyFailureException ex) {
				log.info("Error running transaction", ex);
				// Deadlock fail
				failureException = ex;
			} catch (DataAccessResourceFailureException ex){
				log.info("Error running transaction", ex);
				// Connection fail
				failureException = ex;
			}
		} while (numAttempts++ < this.maxRetries);

		log.error("Transaction couldnt' be retried: "+ pjp.toString());

		throw failureException;
	}
}
