package test;

import java.util.Properties;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.junit.jupiter.api.BeforeAll;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jms.AtomikosConnectionFactoryBean;

public class AtomikosTransactionTest extends AbstractTransactionTest {

	@BeforeAll
	public static void setup() {
		AbstractTransactionTest.userTransaction = new UserTransactionManager();

		AtomikosConnectionFactoryBean connectionFactory = new AtomikosConnectionFactoryBean();
		connectionFactory.setMaxPoolSize(10);
		connectionFactory.setLocalTransactionMode(false); // setting this to TRUE will FORCE the location transaction mode
		connectionFactory.setIgnoreSessionTransactedFlag(false); // use local transaction mode if no global transaction is running
		connectionFactory.setUniqueResourceName("localAtomikos");

		connectionFactory.setXaConnectionFactoryClassName(ActiveMQXAConnectionFactory.class.getName());
		Properties properties = new Properties();
		properties.put("brokerURL", "vm://localhost?broker.persistent=false");

		connectionFactory.setXaProperties(properties);

		AbstractTransactionTest.connectionFactory = connectionFactory;
	}

}
