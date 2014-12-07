package test;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionTestEntity {

	public TransactionTestEntity() {
	}

	@Id
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
