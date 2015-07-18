package test;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

public abstract class BaseTest {

	protected DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=60");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	protected EntityManagerFactory createEntityManagerFactory(
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

}
