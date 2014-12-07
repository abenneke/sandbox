package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TransactionRequiredException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionTest {

	@Autowired
	private PlatformTransactionManager transactionManager;

	// Note:
	// Using TransactionTemplate (instead of @Transactional) here to have fine
	// grained control.
	protected void transaction(final Propagation propagation,
			final TransactionCallbackWithoutResult executeInTransaction) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		transaction.setPropagationBehavior(propagation.value());
		transaction.execute(executeInTransaction);
	}

	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * Dump the database using plain JDBC.
	 */
	protected String dumpJdbc() {
		return jdbc.query(
				"select value from "
						+ TransactionTestEntity.class.getSimpleName()
						+ " order by id", new ResultSetExtractor<String>() {
					public String extractData(final ResultSet rs)
							throws SQLException, DataAccessException {
						final StringBuilder result = new StringBuilder();
						while (rs.next()) {
							if (result.length() > 0) {
								result.append(", ");
							}
							result.append(rs.getString(1));
						}
						return result.toString();
					}
				});
	}

	/**
	 * Insert a {@link TransactionTestEntity} using plain JDBC.
	 */
	protected void insert(final int id, String value) {
		assertEquals(
				1,
				jdbc.update(
						"insert into "
								+ TransactionTestEntity.class.getSimpleName()
								+ " (id, value) values (?, ?)", id, value));
	}

	/**
	 * Clear the table after each test.
	 */
	@After
	public void cleanup() {
		jdbc.update("delete from "
				+ TransactionTestEntity.class.getSimpleName());
	}

	@PersistenceContext
	private EntityManager em;

	/**
	 * Dump the database using JPA.
	 */
	protected String dumpJpa() {
		final StringBuilder result = new StringBuilder();
		for (final TransactionTestEntity e : em.createQuery(
				"select e from " + TransactionTestEntity.class.getSimpleName()
						+ " e order by id", TransactionTestEntity.class)
				.getResultList()) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(e.getValue());
		}
		return result.toString();
	}

	/**
	 * Insert a {@link TransactionTestEntity} using JPA.
	 */
	protected void persist(int id, String value) {
		TransactionTestEntity entity = new TransactionTestEntity();
		entity.setId(id);
		entity.setValue(value);
		em.persist(entity);
	}

	/**
	 * Test transaction management without any transaction.
	 */
	@Test
	public void noTransaction() {

		insert(1, "insert");
		try {
			persist(2, "persist");
			fail("Missing TransactionRequriedException");
		} catch (TransactionRequiredException e) {
			// Question B: Why is this inconsistent?
			// noTransaction: TransactionRequiredException
			// SUPPORTS: no Exception and no effect
			// open EntityManager: no Exception and no effect
		}

		assertEquals("insert", dumpJdbc());
		assertEquals("insert", dumpJpa());
	}

	/**
	 * Test transaction management with a SUPPORTS transaction.
	 */
	@Test
	public void supports() {

		insert(1, "outsideInsert");

		assertEquals("outsideInsert", //
				dumpJdbc());
		assertEquals("outsideInsert", //
				dumpJpa());

		transaction(SUPPORTS, new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertEquals("outsideInsert", //
						dumpJdbc());
				assertEquals("outsideInsert", //
						dumpJpa());

				// Note: We need JPA to see changes done via JDBC in the same
				// transaction.
				insert(3, "insideInsert");

				// Question B: Why is this inconsistent?
				// noTransaction: TransactionRequiredException
				// SUPPORTS: no Exception and no effect
				// open EntityManager: no Exception and no effect
				persist(4, "insidePersist");

				assertEquals("outsideInsert, " //
						+ "insideInsert", //
						dumpJdbc());
				assertEquals("outsideInsert, " //
						+ "insideInsert", //
						dumpJpa());

			}
		});

		// committed
		assertEquals("outsideInsert, " //
				+ "insideInsert", //
				dumpJdbc());
		assertEquals("outsideInsert, " //
				+ "insideInsert", //
				dumpJpa());
	}

	/**
	 * Test transaction management with a REQUIRED transaction.
	 */
	// Note: This works as expected...
	@Test
	public void required() {

		insert(1, "outsideInsert");

		assertEquals("outsideInsert", //
				dumpJdbc());
		assertEquals("outsideInsert", //
				dumpJpa());

		transaction(REQUIRED, new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertEquals("outsideInsert", //
						dumpJdbc());
				assertEquals("outsideInsert", //
						dumpJpa());

				// Note: We need JPA to see changes done via JDBC in the same
				// transaction.
				insert(3, "insideInsert");
				persist(4, "insidePersist");

				assertEquals("outsideInsert, " //
						+ "insideInsert",
				// insidePersist is not yet flushed
						dumpJdbc());
				// the dump-query does a flush
				assertEquals("outsideInsert, " //
						+ "insideInsert, insidePersist", //
						dumpJpa());
				assertEquals("outsideInsert, " //
						+ "insideInsert, insidePersist",
						dumpJdbc());

			}
		});

		// committed
		assertEquals("outsideInsert, " //
				+ "insideInsert, insidePersist", //
				dumpJdbc());
		assertEquals("outsideInsert, " //
				+ "insideInsert, insidePersist", //
				dumpJpa());
	}

	/**
	 * Test transaction management with a REQUIRED transaction which is rolled back.
	 */
	// Note: This works as expected...
	@Test
	public void requiredRollback() {

		insert(1, "outsideInsert");

		assertEquals("outsideInsert", //
				dumpJdbc());
		assertEquals("outsideInsert", //
				dumpJpa());

		transaction(REQUIRED, new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertEquals("outsideInsert", //
						dumpJdbc());
				assertEquals("outsideInsert", //
						dumpJpa());

				// Note: We need JPA to see changes done via JDBC in the same
				// transaction.
				insert(3, "insideInsert");
				persist(4, "insidePersist");

				assertEquals("outsideInsert, " //
						+ "insideInsert",
				// insidePersist is not yet flushed
						dumpJdbc());
				// the dump-query does a flush
				assertEquals("outsideInsert, " //
						+ "insideInsert, insidePersist", //
						dumpJpa());
				assertEquals("outsideInsert, " //
						+ "insideInsert, insidePersist",
						dumpJdbc());

				// trigger rollback
				status.setRollbackOnly();
			}
		});

		// inner changes are rolled back
		assertEquals("outsideInsert", //
				dumpJdbc());
		assertEquals("outsideInsert", //
				dumpJpa());
	}

	@PersistenceUnit
	private EntityManagerFactory emf;

	/**
	 * Execute the runnable with an open entity manager associated with the
	 * thread. The implementation is taken from
	 * {@link OpenEntityManagerInViewInterceptor} or
	 * {@link OpenEntityManagerInViewFilter}.
	 */
	protected void openEntityManager(Runnable runnable) {
		boolean participate = false;
		if (TransactionSynchronizationManager.hasResource(emf)) {
			participate = true;
		} else {
			EntityManager em = emf.createEntityManager();
			EntityManagerHolder emHolder = new EntityManagerHolder(em);
			TransactionSynchronizationManager.bindResource(emf, emHolder);
		}

		try {
			runnable.run();
		} finally {
			if (!participate) {
				EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
						.unbindResource(emf);
				EntityManagerFactoryUtils.closeEntityManager(emHolder
						.getEntityManager());
			}
		}
	}

	/**
	 * Test open entity manager and transaction management without any transaction.
	 */
	@Test
	public void openEntityManagerNoTransaction() {
		openEntityManager(new Runnable() {
			public void run() {
				insert(1, "insert");

				// Question B: Why is this inconsistent?
				// noTransaction: TransactionRequiredException
				// SUPPORTS: no Exception and no effect
				// open EntityManager: no Exception and no effect
				persist(2, "persist");

				assertEquals("insert", dumpJdbc());
				assertEquals("insert", dumpJpa());
			}
		});
	}
	
	/**
	 * Test open entity manager and transaction management with a SUPPORTS transaction.
	 */
	@Test
	public void openEntityManagerSupports() {
		
		// Question A: Why does the "joinTransaction()" call in EntityManagerFactoryUtils.doGetTransactionalEntityManager
		// log this warning in Hibernate? 
		// WARN: HHH000326: Cannot join transaction: do not override hibernate.transaction.factory_class?
		// Should joinTransaction() be invoked only if TransactionSynchronizationManager.isActualTransactionActive() is true?
		
		openEntityManager(new Runnable() {
			public void run() {
				supports();
			}
		});
	}

	/**
	 * Test open entity manager and transaction management with a REQUIRED transaction.
	 */
	// Note: This works as expected, the open entity manager does not change anything...
	@Test
	public void openEntityManagerRequired() {
		openEntityManager(new Runnable() {
			public void run() {
				required();
			}
		});
	}

	/**
	 * Test open entity manager and transaction management with a REQUIRED transaction which is rolled back.
	 */
	// Note: This works as expected, the open entity manager does not change anything...
	@Test
	public void openEntityManagerRequiredRollback() {
		openEntityManager(new Runnable() {
			public void run() {
				requiredRollback();
			}
		});
	}

}
