package test;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Test;

public class OrderedSetValidationTest {

	private void testOrderedSet(Configuration<?> configuration) {
		OrderedSet<InvalidModel> orderedSet = new OrderedSet<>();
		orderedSet.add(new InvalidModel());
		Model model = new Model();
		model.setData(orderedSet);

		Validator validator = configuration.buildValidatorFactory().getValidator();
		Set<ConstraintViolation<Object>> results = validator.validate(model);

		String paths = results.stream().map(c -> c.getPropertyPath().toString())//
				.sorted().collect(Collectors.joining(", "));

		assertEquals("dataAsList[0].notNullProperty, " + //
		// the implementation still allows index based access:
				"dataAsObject[0].notNullProperty, " //
				+ "dataAsRandomAccess[0].notNullProperty, " //
				+ "dataAsSerializable[0].notNullProperty, " //
				+ "dataAsSet[0].notNullProperty", paths);
	}

	private Configuration<?> baseConfiguration() {
		return Validation.byDefaultProvider().configure()
				// ParameterMessageInterpolator just to avoid the EL dependency
				.messageInterpolator(new ParameterMessageInterpolator());
	}

	@Test
	public void testOrderedSetWithoutExtractor() {
		testOrderedSet(baseConfiguration());
	}

	@Test
	public void testOrderedSetWithExtractor() {
		testOrderedSet(baseConfiguration().addValueExtractor(new OrderedSetValueExtractor()));
	}

}