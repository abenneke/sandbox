package test;

import static org.junit.Assert.assertEquals;

import javax.validation.TraversableResolver;

import org.hibernate.validator.internal.engine.resolver.TraversableResolvers;
import org.junit.Test;

public class JPATraversableResolverTest {

	@Test
	public void testDefaultResolver() {
		// JPA is on the classpath, 
		// so we expect to get the JPATraversableResolver
		TraversableResolver defaultResolver = TraversableResolvers.getDefault();
		assertEquals(defaultResolver.getClass().getSimpleName(), "JPATraversableResolver");
	}
}