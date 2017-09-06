package test;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class TestEntity {

	private String id;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Boolean objectBoolean;

	public Boolean getObjectBoolean() {
		return objectBoolean;
	}

	public void setObjectBoolean(Boolean objectBoolean) {
		this.objectBoolean = objectBoolean;
	}

	private boolean primitiveBoolean;

	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
	}

	public void setPrimitiveBoolean(boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}

	private boolean userTypePrimitiveBoolean;

	@Type(type = "test.YNPrimitiveBooleanUserType")
	public boolean isUserTypePrimitiveBoolean() {
		return userTypePrimitiveBoolean;
	}

	public void setUserTypePrimitiveBoolean(boolean userTypePrimitiveBoolean) {
		this.userTypePrimitiveBoolean = userTypePrimitiveBoolean;
	}

}
