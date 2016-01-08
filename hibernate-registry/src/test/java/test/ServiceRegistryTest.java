package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;

public class ServiceRegistryTest {

	protected ServiceRegistry buildRegistry() {
		return new StandardServiceRegistryBuilder().addInitiator(new TestServiceInitiator()).build();
	}

	@Test
	public void testSingleThread() {
		ServiceRegistry registry = buildRegistry();

		TestService service = registry.getService(TestService.class);

		assertTrue(service.isInitialized());
	}

	@SuppressWarnings("unchecked")
	private <T> Future<T>[] runParallel(int count, Callable<T> callable)
			throws InterruptedException, ExecutionException {
		Future<T>[] results = new Future[count];
		Thread[] threads = new Thread[count];
		for (int i = 0; i < count; i++) {
			FutureTask<T> task = new FutureTask<>(callable);
			results[i] = task;
			threads[i] = new Thread(task);
		}
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
		return results;
	}

	@Test
	public void testMultipleCreate() throws InterruptedException, ExecutionException {
		final ServiceRegistry registry = buildRegistry();

		Future<Integer>[] serviceIdentity = runParallel(100, new Callable<Integer>() {
			public Integer call() throws InterruptedException {
				TestService service = registry.getService(TestService.class);
				return Integer.valueOf(System.identityHashCode(service));
			};
		});

		// there should be created only one instance per registry?
		Set<Integer> uniqueServices = new HashSet<Integer>();
		for (Future<Integer> service : serviceIdentity) {
			uniqueServices.add(service.get());
		}
		assertEquals(1, uniqueServices.size());
	}

	@Test
	public void testMultipleInitialize() throws InterruptedException, ExecutionException {
		final ServiceRegistry registry = buildRegistry();

		int count = 100;
		Future<Boolean>[] serviceInitialized = runParallel(count, new Callable<Boolean>() {
			public Boolean call() throws InterruptedException {
				TestService service = registry.getService(TestService.class);
				// remember if the service was initialized when received from
				// registry
				return Boolean.valueOf(service.isInitialized());
			};
		});

		// all services should be initialized when returned from registry
		int initialized = 0;
		for (Future<Boolean> service : serviceInitialized) {
			if (service.get().booleanValue()) {
				initialized++;
			}
		}
		assertEquals(count, initialized);
	}

}
