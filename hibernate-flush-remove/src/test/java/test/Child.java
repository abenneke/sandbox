package test;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Child extends BaseEntity {

	private Parent parent;

	@ManyToOne(optional = false)
	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

}
