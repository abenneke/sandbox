package test;

import javax.validation.constraints.NotNull;

public class InvalidModel {

	@NotNull
	public Object getNotNullProperty() {
		return null; // just for testing purposes
	}

}
