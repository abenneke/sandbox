package test;

import java.util.List;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.validator.internal.engine.path.NodeImpl;

public class OrderedSetValueExtractor implements ValueExtractor<OrderedSet<@ExtractedValue ?>> {

	@Override
	public void extractValues(OrderedSet<?> originalValue, ValueExtractor.ValueReceiver receiver) {
		for (int i = 0; i < originalValue.size(); i++) {
			receiver.indexedValue(NodeImpl.LIST_ELEMENT_NODE_NAME, i, ((List<?>) originalValue).get(i));
		}
	}

}
