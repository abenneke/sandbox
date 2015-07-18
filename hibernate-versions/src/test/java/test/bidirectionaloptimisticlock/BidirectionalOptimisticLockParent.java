package test.bidirectionaloptimisticlock;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OptimisticLock;

import test.BaseParentEntity;

@Entity
public class BidirectionalOptimisticLockParent extends BaseParentEntity {

	private Set<BidirectionalOptimisticLockChild> children = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
	@OptimisticLock(excluded = false)
	public Set<BidirectionalOptimisticLockChild> getChildren() {
		return children;
	}

	public void setChildren(Set<BidirectionalOptimisticLockChild> children) {
		this.children = children;
	}

	@Override
	public void addNewChild(String id) {
		BidirectionalOptimisticLockChild child = new BidirectionalOptimisticLockChild();
		child.setId(id);
		child.setParent(this);
		getChildren().add(child);
	}

	@Override
	public void removeChild() {
		BidirectionalOptimisticLockChild childToRemove = getChildren()
				.iterator().next();
		childToRemove.setParent(null);
		getChildren().remove(childToRemove);
	}

}
