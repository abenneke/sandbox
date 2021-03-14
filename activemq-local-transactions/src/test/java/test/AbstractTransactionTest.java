package test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.transaction.UserTransaction;

import org.junit.jupiter.api.Test;

public abstract class AbstractTransactionTest {

	protected static UserTransaction userTransaction;

	protected static ConnectionFactory connectionFactory;

	@Test
	public void distributedTransaction() throws Exception {

		userTransaction.begin();

		Connection connection = connectionFactory.createConnection();
		try {
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			try {
				sendTestMessage(session);
			} finally {
				session.close();
			}
		} finally {
			connection.close();
		}

		userTransaction.commit();
	}

	@Test
	public void localTransaction() throws JMSException {

		Connection connection = connectionFactory.createConnection();
		try {
			Session session = connection.createSession(false, Session.SESSION_TRANSACTED);
			try {
				sendTestMessage(session);
			} finally {
				session.close();
			}
		} finally {
			connection.close();
		}
	}

	private void sendTestMessage(Session session) throws JMSException {
		Queue queue = session.createQueue("test");
		MessageProducer messageProducer = session.createProducer(queue);
		try {
			messageProducer.send(session.createTextMessage("test"));
		} finally {
			messageProducer.close();
		}
	}

}
