package es.itest.engine.test.business.entity;

public final class DomainEvents {

	public static final DomainEvent<ItemResponse> ITEM_RESPONSE_USED = new DomainEvent<ItemResponse>();

	public static DomainEvent<Item> ITEM_USED = new DomainEvent<Item>();;
	
	private DomainEvents() {
		// Avoid instantiation
	}
}
