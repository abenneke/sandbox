package test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;

public class OrphanChildrenNullCollectionTest {

	private DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=60");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	private EntityManagerFactory createEntityManagerFactory(
			DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(Environment.DATASOURCE, dataSource);
		properties.put(Environment.DIALECT, H2Dialect.class.getName());
		properties.put(Environment.HBM2DDL_AUTO, "create-drop");
		properties.put(Environment.SHOW_SQL, "true");
		properties.put(Environment.CHECK_NULLABILITY, "false");

		return new HibernatePersistenceProvider().createEntityManagerFactory(
				"test", properties);
	}

	protected void testDanglingCollection(boolean emptySet)
	{
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		try {
			Parent parent = new Parent();
			parent.setId("parent");
			parent.setValue("parent");

			if (emptySet) {
				parent.setChildren(Collections.<Child>emptySet());
			}
			// else: leave children null!

			// save this initial state
			em.persist(parent);
			em.flush();

			// clear to detach all entities and to reset the entity manager
			em.clear();

			// merge it back
			parent = em.merge(parent);

			// change again (not strictly necessary to trigger the exception)
			parent.setValue("changed");

			// and save those changes
			em.flush();
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

	}

	@Test
	public void testChildrenEmptySet() {
		testDanglingCollection(true);
	}

	@Test
	public void testChildrenNull() {
		testDanglingCollection(false);
	}

}
