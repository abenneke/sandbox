package test;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;

import test.BaseTest;

/**
 * Base test for aspects of an entity where the parent is owner of a
 * parent/child relationship.
 */
public abstract class BaseParentOwnedTest extends BaseTest {

	/**
	 * Factory method for a new parent entity.
	 */
	protected abstract BaseParentEntity createParentEntity(String id);

	@Test
	public void addChildParentVersion() {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		try {
			// create a parent with one child
			BaseParentEntity parent = createParentEntity("parent");
			parent.addNewChild("first");
			em.persist(parent);
			em.flush();

			// initial version
			assertEquals(0, parent.getVersion());

			// add another child
			parent.addNewChild("second");
			em.flush();

			// since the parent is the owner,
			// the parent version is expected to be updated
			// on changes to the children collection
			assertEquals(1, parent.getVersion());
		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void removeChildParentVersion() {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		try {
			// create a parent with two children
			BaseParentEntity parent = createParentEntity("parent");
			parent.addNewChild("first");
			parent.addNewChild("second");
			em.persist(parent);
			em.flush();

			// initial version
			assertEquals(0, parent.getVersion());

			// remove one (random) child
			parent.removeChild();
			em.flush();

			// since the parent is the owner,
			// the parent version is expected to be updated
			// on changes to the children collection
			assertEquals(1, parent.getVersion());
		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

}
