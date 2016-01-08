package test;

import java.util.Map;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryImplementor;

public class TestServiceInitiator implements StandardServiceInitiator<TestService> {

	@Override
	public Class<TestService> getServiceInitiated() {
		return TestService.class;
	}

	@Override
	public TestService initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		return new TestService();
	}

}
