package test.bidirectionaloptimisticlock;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import test.BaseEntity;

@Entity
public class BidirectionalOptimisticLockChild extends BaseEntity {

	private BidirectionalOptimisticLockParent parent;

	// optional = false to make it mandatory
	@ManyToOne(optional = false)
	@JoinColumn(name = "parent_id")
	public BidirectionalOptimisticLockParent getParent() {
		return parent;
	}

	public void setParent(BidirectionalOptimisticLockParent parent) {
		this.parent = parent;
	}

}
