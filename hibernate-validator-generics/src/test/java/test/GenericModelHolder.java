package test;

import javax.validation.Valid;

public class GenericModelHolder<M> {

	private M model;

	@Valid
	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

}
