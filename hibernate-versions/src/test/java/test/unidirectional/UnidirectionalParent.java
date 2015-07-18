package test.unidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import test.BaseParentEntity;

@Entity
public class UnidirectionalParent extends BaseParentEntity {

	private Set<UnidirectionalChild> children = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	public Set<UnidirectionalChild> getChildren() {
		return children;
	}

	public void setChildren(Set<UnidirectionalChild> children) {
		this.children = children;
	}

	@Override
	public void addNewChild(String id) {
		UnidirectionalChild child = new UnidirectionalChild();
		child.setId(id);
		getChildren().add(child);
	}

	@Override
	public void removeChild() {
		getChildren().remove(getChildren().iterator().next());
	}

}
