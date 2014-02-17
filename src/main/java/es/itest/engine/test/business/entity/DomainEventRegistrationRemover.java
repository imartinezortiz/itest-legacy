package es.itest.engine.test.business.entity;

public class DomainEventRegistrationRemover implements AutoCloseable {
	
	private final Action0 callOnRemoval;

	public DomainEventRegistrationRemover(Action0 callOnRemoval) {
		this.callOnRemoval = callOnRemoval; 
	}

	@Override
	public void close() {
		this.callOnRemoval.execute();;
	}

}