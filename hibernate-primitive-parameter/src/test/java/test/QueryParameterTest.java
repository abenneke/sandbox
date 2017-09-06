package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;

public class QueryParameterTest {

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
		properties.put(Environment.SHOW_SQL, "true");
		properties.put(Environment.CHECK_NULLABILITY, "false");

		return new HibernatePersistenceProvider().createEntityManagerFactory("test", properties);
	}

	protected void testQuery(Function<EntityManager, Query> queryBuilder) {
		DataSource dataSource = createDataSource();
		EntityManagerFactory emf = createEntityManagerFactory(dataSource);
		EntityManager em = emf.createEntityManager();
		try {
			Query query = queryBuilder.apply(em);
			// just make sure, the query is executable
			assertEquals(0, query.getResultList().size());
		} finally {
			em.close();
		}
	}

	@Test
	public void queryObjectBoolean() {
		testQuery(em -> {
			Query query = em.createQuery("select e from TestEntity e where e.objectBoolean = :value");
			query.setParameter("value", Boolean.TRUE);
			return query;
		});
	}

	@Test
	public void queryPrimitiveBoolean() {
		testQuery(em -> {
			Query query = em.createQuery("select e from TestEntity e where e.primitiveBoolean = :value");
			query.setParameter("value", Boolean.TRUE);
			return query;
		});
	}

	@Test
	public void criteriaEqualObjectBoolean() {
		testQuery(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestEntity> query = cb.createQuery(TestEntity.class);
			Root<TestEntity> root = query.from(TestEntity.class);
			query.where(cb.equal(root.get(TestEntity_.objectBoolean), Boolean.TRUE));
			return em.createQuery(query);
		});
	}

	@Test
	public void criteriaEqualPrimitiveBoolean() {
		testQuery(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestEntity> query = cb.createQuery(TestEntity.class);
			Root<TestEntity> root = query.from(TestEntity.class);
			query.where(cb.equal(root.get(TestEntity_.primitiveBoolean), Boolean.TRUE));
			return em.createQuery(query);
		});
	}

	@Test
	public void criteriaIsTrueObjectBoolean() {
		testQuery(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestEntity> query = cb.createQuery(TestEntity.class);
			Root<TestEntity> root = query.from(TestEntity.class);
			query.where(cb.isTrue(root.get(TestEntity_.objectBoolean)));
			return em.createQuery(query);
		});
	}

	@Test
	public void criteriaIsTruePrimitiveBoolean() {
		testQuery(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TestEntity> query = cb.createQuery(TestEntity.class);
			Root<TestEntity> root = query.from(TestEntity.class);
			query.where(cb.isTrue(root.get(TestEntity_.primitiveBoolean)));
			return em.createQuery(query);
		});
	}

}
