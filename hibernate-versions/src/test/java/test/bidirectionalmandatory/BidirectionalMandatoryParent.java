package test.bidirectionalmandatory;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import test.BaseParentEntity;

@Entity
public class BidirectionalMandatoryParent extends BaseParentEntity {

	private Set<BidirectionalMandatoryChild> children = new HashSet<>();

	// Not mappedBy to make this the owner of the relation
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	public Set<BidirectionalMandatoryChild> getChildren() {
		return children;
	}

	public void setChildren(Set<BidirectionalMandatoryChild> children) {
		this.children = children;
	}

	@Override
	public void addNewChild(String id) {
		BidirectionalMandatoryChild child = new BidirectionalMandatoryChild();
		child.setId(id);
		child.setParent(this);
		getChildren().add(child);
	}

	@Override
	public void removeChild() {
		BidirectionalMandatoryChild childToRemove = getChildren().iterator()
				.next();
		childToRemove.setParent(null);
		getChildren().remove(childToRemove);
	}

}
