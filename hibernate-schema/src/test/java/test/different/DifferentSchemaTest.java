package test.different;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.Test;

import test.BaseTest;

public class DifferentSchemaTest extends BaseTest {


	protected void startup(String ddl) throws SQLException {
		DataSource dataSource = createDataSource(ddl);

		// keep a connection open during test
		// to avoid premature destruction of the memory database
		try (Connection c = dataSource.getConnection()) {

			try (Statement s = c.createStatement()) {
				s.execute("create schema FIRST");
				s.execute("create schema SECOND");
			}

			Map<String, Object> properties = new HashMap<>();
			properties.put(Environment.DATASOURCE, dataSource);
			properties.put(Environment.DIALECT, H2Dialect.class.getName());
			properties.put(Environment.HBM2DDL_AUTO, ddl);
			properties.put(Environment.SHOW_SQL, "true");
			properties.put(Environment.CHECK_NULLABILITY, "false");

			new HibernatePersistenceProvider().createEntityManagerFactory(
					"test", properties);
		}
	}

	@Test
	public void create() throws SQLException {
		startup("create");
	}

	@Test
	public void update() throws SQLException {
		startup("update");
	}

}
