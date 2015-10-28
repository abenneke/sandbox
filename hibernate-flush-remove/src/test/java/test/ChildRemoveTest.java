package test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;

public class ChildRemoveTest {

	private DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:transactionTest;DB_CLOSE_DELAY=60");
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

		return new HibernatePersistenceProvider().createEntityManagerFactory(
				"ChildRemoveTest", properties);
	}

	protected void removeChild(boolean persist, boolean flush, boolean clearParent) {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			// create a parent with two children
			Parent parent = new Parent();
			parent.setId("parent");
			HashSet<Child> children = new HashSet<>();

			Child first = new Child();
			first.setId("first");
			first.setParent(parent);
			children.add(first);

			Child second = new Child();
			second.setId("second");
			second.setParent(parent);
			children.add(second);

			parent.setChildren(children);

			// save the parent
			if (persist) {
				em.persist(parent);
			} else {
				parent = em.merge(parent);
			}
			if (flush) {
				em.flush();
			}

			// there should be two children
			assertEquals(2, parent.getChildren().size());

			// remove one child
			final Child childToRemove = parent.getChildren().iterator().next();
			parent.getChildren().remove(childToRemove);
			if (clearParent) {
				childToRemove.setParent(null);
			}
			em.flush();

			// reset the entity manager to drop all cached entites
			em.clear();

			// query the database to check if the child is really gone
			final CriteriaBuilder cb = em.getCriteriaBuilder();
			final CriteriaQuery<Parent> query = cb.createQuery(Parent.class);
			final Root<Parent> from = query.from(Parent.class);
			query.where(cb.equal(from.<String> get("id"), "parent"));
			final Parent loadedParent = em.createQuery(query).getSingleResult();
			assertEquals(1, loadedParent.getChildren().size());

		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void testPersistNoFlushNoClearParent() throws SQLException {
		removeChild(true, false, false);
	}

	@Test
	public void testPersistFlushNoClearParent() throws SQLException {
		removeChild(true, true, false);
	}

	@Test
	public void testPersistNoFlushClearParent() throws SQLException {
		removeChild(true, false, true);
	}

	@Test
	public void testPersistFlushClearParent() throws SQLException {
		removeChild(true, true, true);
	}

	@Test
	public void testMergeNoFlushNoClearParent() throws SQLException {
		removeChild(false, false, false);
	}

	@Test
	public void testMergeFlushNoClearParent() throws SQLException {
		removeChild(false, true, false);
	}

	@Test
	public void testMergeNoFlushClearParent() throws SQLException {
		removeChild(false, false, true);
	}

	@Test
	public void testMergeFlushClearParent() throws SQLException {
		removeChild(false, true, true);
	}

	protected void detachedRemoveChild(boolean clearParent) {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			// create a parent with two children
			Parent parent = new Parent();
			parent.setId("parent");
			HashSet<Child> children = new HashSet<>();

			Child first = new Child();
			first.setId("first");
			first.setParent(parent);
			children.add(first);

			Child second = new Child();
			second.setId("second");
			second.setParent(parent);
			children.add(second);

			parent.setChildren(children);

			// save the parent
			em.persist(parent);
			em.flush();

			// and detach it
			em.detach(parent);

			// remove one child
			final Child childToRemove = parent.getChildren().iterator().next();
			parent.getChildren().remove(childToRemove);
			if (clearParent) {
				childToRemove.setParent(null);
			}

			// merge the changed parent back
			parent = em.merge(parent);

			// the child should still be gone now?
			assertEquals(1, parent.getChildren().size());

			// save the change
			em.flush();

			// reset the entity manager to drop all cached entites
			em.clear();

			// query the database to check if the child is really gone
			final CriteriaBuilder cb = em.getCriteriaBuilder();
			final CriteriaQuery<Parent> query = cb.createQuery(Parent.class);
			final Root<Parent> from = query.from(Parent.class);
			query.where(cb.equal(from.<String> get("id"), "parent"));
			final Parent loadedParent = em.createQuery(query).getSingleResult();
			assertEquals(1, loadedParent.getChildren().size());

		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void testDetachedRemoveChildNoClearParent() {
		detachedRemoveChild(false);
	}

	@Test
	public void testDetachedRemoveChildClearParent() {
		detachedRemoveChild(true);
	}

}
