package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.transaction.UserTransaction;

import org.junit.jupiter.api.Test;

public abstract class AbstractTransactionTest {

	protected static UserTransaction userTransaction;

	protected static ConnectionFactory connectionFactory;

	@Test
	public void distributedTransactionSessionTransacted() throws Exception {
		distributedTransaction(Session.SESSION_TRANSACTED);
	}

	@Test
	public void distributedTransactionAutoAcknowledge() throws Exception {

		// this acknowledgeMode is effectively ignored; SESSION_TRANSACTED is used
		// internally
		distributedTransaction(Session.AUTO_ACKNOWLEDGE);
	}

	private void distributedTransaction(int acknowledgeMode) throws Exception {

		String queueName = this.getClass().getSimpleName() + "DistributedTransaction" + acknowledgeMode;

		userTransaction.begin();
		sendMessage(true, acknowledgeMode, queueName, "firstMessage");
		userTransaction.commit();

		userTransaction.begin();
		sendMessage(true, acknowledgeMode, queueName, "rolledBackMessage");
		userTransaction.rollback();

		userTransaction.begin();
		sendMessage(true, acknowledgeMode, queueName, "secondMessage");
		userTransaction.commit();

		userTransaction.begin();
		assertEquals("firstMessage", receiveMessage(true, acknowledgeMode, queueName));
		userTransaction.commit();

		// "rolledBackMessage" was never sent

		userTransaction.begin();
		assertEquals("secondMessage", receiveMessage(true, acknowledgeMode, queueName));
		userTransaction.rollback();

		// receival of secondMessage was rolled back before and can be received again
		userTransaction.begin();
		assertEquals("secondMessage", receiveMessage(true, acknowledgeMode, queueName));
		userTransaction.commit();
	}

	@Test
	public void localTransactionAutoAcknowledge() throws Exception {
		localTransaction(Session.AUTO_ACKNOWLEDGE);
	}

	@Test
	public void localTransactionSessionTransacted() throws Exception {

		// I am not sure if this is the case AMQ-2659 wants to detect/prevent?
		// It might be okay if this is not working - but it at least does/did with
		// ActiveMQ 5.15.14 ...

		localTransaction(Session.SESSION_TRANSACTED);
	}

	private void localTransaction(int acknowledgeMode) throws Exception {

		String queueName = this.getClass().getSimpleName() + "LocalTransaction" + acknowledgeMode;

		sendMessage(false, acknowledgeMode, queueName, "firstMessage");
		sendMessage(false, acknowledgeMode, queueName, "secondMessage");

		assertEquals("firstMessage", receiveMessage(false, acknowledgeMode, queueName));
		assertEquals("secondMessage", receiveMessage(false, acknowledgeMode, queueName));
	}

	private void sendMessage(boolean transacted, int acknowledgeMode, String queueName, String message)
			throws JMSException {
		Connection connection = connectionFactory.createConnection();
		try {
			Session session = connection.createSession(transacted, acknowledgeMode);
			try {
				Queue queue = session.createQueue(queueName);
				MessageProducer messageProducer = session.createProducer(queue);
				try {
					messageProducer.send(session.createTextMessage(message));
				} finally {
					messageProducer.close();
				}
			} finally {
				session.close();
			}
		} finally {
			connection.close();
		}
	}

	private String receiveMessage(boolean transacted, int acknowledgeMode, String queueName) throws JMSException {
		Connection connection = connectionFactory.createConnection();
		try {
			Session session = connection.createSession(transacted, acknowledgeMode);
			try {
				Queue queue = session.createQueue(queueName);
				MessageConsumer messageConsumer = session.createConsumer(queue);

				connection.start();
				Message message = null;
				try {
					message = messageConsumer.receive(1000);
				} finally {
					messageConsumer.close();
				}

				if (message != null) {
					return ((TextMessage) message).getText();
				} else {
					return null;
				}
			} finally {
				session.close();
			}
		} finally {
			connection.close();
		}
	}

}
