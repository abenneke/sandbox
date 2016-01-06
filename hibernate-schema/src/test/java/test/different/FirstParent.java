package test.different;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import test.BaseEntity;

@Entity
@Table(schema="FIRST")
public class FirstParent extends BaseEntity {

	private Set<SecondChild> children = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	public Set<SecondChild> getChildren() {
		return children;
	}

	public void setChildren(Set<SecondChild> children) {
		this.children = children;
	}


}
