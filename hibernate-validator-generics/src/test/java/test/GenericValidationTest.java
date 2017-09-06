package test;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Test;

public class GenericValidationTest {

	protected void assertInvalid(String path, Object object) {
		Validator validator = Validation.byDefaultProvider().configure()
				// ParameterMessageInterpolator just to avoid the EL dependency
				.messageInterpolator(new ParameterMessageInterpolator())//
				.buildValidatorFactory().getValidator();
		Set<ConstraintViolation<Object>> results = validator.validate(object);
		assertEquals(1, results.size());
		assertEquals(path, results.iterator().next().getPropertyPath().toString());
	}

	@Test
	public void testGenericHolderInvalidModel() {
		GenericModelHolder<InvalidModel> holder = new GenericModelHolder<>();
		holder.setModel(new InvalidModel());
		assertInvalid("model.notNullProperty", holder);
	}

	@Test
	public void testGenericHolderInvalidModelArray() {
		GenericModelHolder<InvalidModel[]> holder = new GenericModelHolder<>();
		holder.setModel(new InvalidModel[] { new InvalidModel() });
		assertInvalid("model[0].notNullProperty", holder);
	}

	@Test
	public void testGenericHolderInvalidModelList() {
		GenericModelHolder<List<InvalidModel>> holder = new GenericModelHolder<>();
		holder.setModel(Collections.singletonList(new InvalidModel()));
		assertInvalid("model[0].notNullProperty", holder);
	}

}
