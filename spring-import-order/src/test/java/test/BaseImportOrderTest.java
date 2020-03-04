package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseImportOrderTest {

	@Autowired
	@Qualifier("foo")
	private String foo;
	
	@Test
	public void testFoo() {
		// expecting "fooFromB", because FooConfigurationB is importing FooConfigurationA, 
		// hence overriding the configuration from A with the one from B
		assertEquals("fooFromB", foo); 
	}
	
	@Autowired
	@Qualifier("bar")
	private String bar;

	@Test
	public void testBar() {
		// expecting "barFromA", because BarConfigurationA is importing BarConfigurationB, 
		// hence overriding the configuration from B with the one from A
		assertEquals("barFromA", bar); 
	}

}
