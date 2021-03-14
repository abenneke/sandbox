package test;

import java.util.Properties;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.junit.jupiter.api.BeforeAll;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jms.PoolingConnectionFactory;

public class BitronixTransactionTest extends AbstractTransactionTest {

	@BeforeAll
	public static void setup() {
		AbstractTransactionTest.userTransaction = TransactionManagerServices.getTransactionManager();

		PoolingConnectionFactory connectionFactory = new PoolingConnectionFactory();
		connectionFactory.setMaxPoolSize(10);
		connectionFactory.setAllowLocalTransactions(true);
		connectionFactory.setUniqueName("localActiveMq");

		connectionFactory.setClassName(ActiveMQXAConnectionFactory.class.getName());
		Properties properties = new Properties();
		properties.put("brokerURL", "vm://localhost?broker.persistent=false");

		connectionFactory.setDriverProperties(properties);

		AbstractTransactionTest.connectionFactory = connectionFactory;
	}

}
