package test.bidirectionalmandatory;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import test.BaseEntity;

@Entity
public class BidirectionalMandatoryChild extends BaseEntity {

	private BidirectionalMandatoryParent parent;

	// optional = false to make it mandatory
	@ManyToOne(optional = false)
	public BidirectionalMandatoryParent getParent() {
		return parent;
	}

	public void setParent(BidirectionalMandatoryParent parent) {
		this.parent = parent;
	}

}
