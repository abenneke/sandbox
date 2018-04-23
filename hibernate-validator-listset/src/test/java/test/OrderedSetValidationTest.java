package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Test;

public class OrderedSetValidationTest {

	@Test
	public void testOrderedSet() {
		OrderedSet<InvalidModel> orderedSet = new OrderedSet<>();
		orderedSet.add(new InvalidModel());
		Model model = new Model();
		model.setData(orderedSet);

		Validator validator = Validation.byDefaultProvider().configure()
				// ParameterMessageInterpolator just to avoid the EL dependency
				.messageInterpolator(new ParameterMessageInterpolator())//
				.buildValidatorFactory().getValidator();
		Set<ConstraintViolation<Object>> results = validator.validate(model);
		assertEquals(2, results.size());

		List<String> expectedPaths = Arrays.asList(//
				"dataAsList[0].notNullProperty", //
				// the implementation still allows index based access:
				"dataAsSet[0].notNullProperty");

		for (ConstraintViolation<Object> constraint : results) {
			String path = constraint.getPropertyPath().toString();
			if (!expectedPaths.contains(path)) {
				fail("Found unexpected path " + path);
			}
		}
	}

}
