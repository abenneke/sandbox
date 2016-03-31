package test;

import javax.persistence.Id;
import javax.persistence.*;

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

	// default value for illustration purposes
	private String value = "unhydrated";

	public String getValue() {
		return value;
	}

	public static boolean hydrateable = true;

	public void setValue(String value) {
		if (!hydrateable) {
			// let hydration fail for testing purposes
			// (in real world a UserType failed to convert the given value,
			// but this is enough to demonstrate the problem)
			throw new IllegalStateException("Unable to hydrate");
		}

		this.value = value;
	}

}
