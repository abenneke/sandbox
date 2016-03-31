package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;

public class HydrateTest {

	protected DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=60");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	protected EntityManagerFactory createEntityManagerFactory(DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(Environment.DATASOURCE, dataSource);
		properties.put(Environment.DIALECT, H2Dialect.class.getName());
		properties.put(Environment.HBM2DDL_AUTO, "create-drop");

		return new HibernatePersistenceProvider().createEntityManagerFactory("test", properties);
	}

	protected EntityManagerFactory setup() throws SQLException {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);

		// create a single test record
		Connection connection = dataSource.getConnection();
		connection.createStatement().execute("insert into TestEntity (id, value) values ('1', 'first')");
		connection.close();

		return emf;
	}

	@Test
	public void testHydrateable() throws SQLException {
		TestEntity.hydrateable = true;

		EntityManagerFactory emf = setup();

		EntityManager em = emf.createEntityManager();

		TestEntity a = em.find(TestEntity.class, "1");
		assertNotNull(a);
		assertEquals("first", a.getValue());

		TestEntity b = em.find(TestEntity.class, "1");
		assertNotNull(b);
		assertEquals("first", b.getValue());

		assertSame(a, b);
	}

	@Test
	public void testNotHydrateable() throws SQLException {
		TestEntity.hydrateable = false;

		EntityManagerFactory emf = setup();

		EntityManager em = emf.createEntityManager();

		try {
			TestEntity a = em.find(TestEntity.class, "1");
			fail("Missing exception while loading entity for the first time, got: " + "id=" + a.getId() + ", value="
					+ a.getValue());
		} catch (PersistenceException e) {
			// expected
		}

		try {
			TestEntity b = em.find(TestEntity.class, "1");
			fail("Missing exception while loading entity a second time, got: " + "id=" + b.getId() + ", value="
					+ b.getValue());
		} catch (PersistenceException e) {
			// expected
		}
	}

}
