package test.unidirectional;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.collection.spi.PersistentCollection;
import org.junit.Test;

import test.BaseTest;

public class NegativeVersionTest extends BaseTest {

	protected void save(boolean merge, boolean negative) {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		try {
			UnidirectionalParent parent = new UnidirectionalParent();
			parent.setId("parent");
			parent.setValue("parent");
			if (negative) {
				parent.setVersion(-1);
			}

			UnidirectionalChild child = new UnidirectionalChild();
			child.setId("child");
			child.setValue("child");
			parent.setChildren(Collections
					.<UnidirectionalChild> singleton(child));

			if (merge) {
				parent = em.merge(parent);
			} else {
				em.persist(parent);
			}

			// expect the collection to be persistent after the save
			assertTrue(parent.getChildren().getClass().toString(),
					parent.getChildren() instanceof PersistentCollection);

		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void persistPositive() {
		save(false, false);
	}

	@Test
	public void persistNegative() {
		save(false, true);
	}

	@Test
	public void mergePositive() {
		save(true, false);
	}

	@Test
	public void mergeNegative() {
		save(true, true);
	}
}
