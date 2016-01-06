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
@Table(schema="SECOND")
public class SecondParent extends BaseEntity {

	private Set<FirstChild> children = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	public Set<FirstChild> getChildren() {
		return children;
	}

	public void setChildren(Set<FirstChild> children) {
		this.children = children;
	}


}
