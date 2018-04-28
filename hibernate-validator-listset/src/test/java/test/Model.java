package test;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

public class Model {

	private OrderedSet<InvalidModel> data;

	public void setData(OrderedSet<InvalidModel> data) {
		this.data = data;
	}
	
	@Valid
	public Set<InvalidModel> getDataAsSet() {
		return data;
	}

	@Valid
	public List<InvalidModel> getDataAsList() {
		return data;
	}

	@Valid
	public Object getDataAsObject() {
		return data;
	}

	@Valid
	public Serializable getDataAsSerializable() {
		return data;
	}

}
