package test.bidirectionaloptional;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import test.BaseEntity;

@Entity
public class BidirectionalOptionalChild extends BaseEntity {

	private BidirectionalOptionalParent parent;

	// optional = true is default, just repeated here to clarify
	@ManyToOne(optional = true)
	public BidirectionalOptionalParent getParent() {
		return parent;
	}

	public void setParent(BidirectionalOptionalParent parent) {
		this.parent = parent;
	}

}
