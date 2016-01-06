package test;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public abstract class BaseTest {

	protected DataSource createDataSource(String name) {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:" + name);
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

}
