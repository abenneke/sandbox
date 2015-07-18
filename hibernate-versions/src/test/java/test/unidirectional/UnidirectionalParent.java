package test.unidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import test.BaseEntity;

@Entity
public class UnidirectionalParent extends BaseEntity {

	private Set<UnidirectionalChild> children = new HashSet<>();

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval= true)
	@JoinColumn(name = "parent_id")
	public Set<UnidirectionalChild> getChildren() {
		return children;
	}

	public void setChildren(Set<UnidirectionalChild> children) {
		this.children = children;
	}

	   @Override
	   public void setVersion(final int version)
	   {
	      new Throwable(String.valueOf(version)).printStackTrace();
	      super.setVersion(version);
	   }


}
