package test;

public abstract class BaseParentEntity extends BaseEntity {

	/**
	 * Helper to create and add a new child.
	 */
	public abstract void addNewChild(String id);

	/**
	 * Helper to remove a (random) child.
	 */
	public abstract void removeChild();

}
