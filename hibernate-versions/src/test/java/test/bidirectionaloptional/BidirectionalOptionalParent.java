package test.bidirectionaloptional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import test.BaseParentEntity;

@Entity
public class BidirectionalOptionalParent extends BaseParentEntity {

	private Set<BidirectionalOptionalChild> children = new HashSet<>();

	// Not mappedBy to make this the owner of the relation
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	public Set<BidirectionalOptionalChild> getChildren() {
		return children;
	}

	public void setChildren(Set<BidirectionalOptionalChild> children) {
		this.children = children;
	}

	@Override
	public void addNewChild(String id) {
		BidirectionalOptionalChild child = new BidirectionalOptionalChild();
		child.setId(id);
		child.setParent(this);
		getChildren().add(child);
	}

	@Override
	public void removeChild() {
		BidirectionalOptionalChild childToRemove = getChildren().iterator()
				.next();
		childToRemove.setParent(null);
		getChildren().remove(childToRemove);
	}

}
