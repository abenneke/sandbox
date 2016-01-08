package test;

import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

public class TestService implements ServiceRegistryAwareService, Service {

	public TestService() {
		// sleep to simulate slow creation
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}

	private boolean initialized;

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {

		// sleep to simulate slow initialization
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
