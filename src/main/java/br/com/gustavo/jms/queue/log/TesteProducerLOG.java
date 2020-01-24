package br.com.gustavo.jms.queue.log;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteProducerLOG {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		InitialContext context = new InitialContext();

		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();

		/*
		 * O primeiro parâmetro do método createSession define se queremos usar o
		 * tratamento da transação como explícito. Como colocamos false, não é preciso
		 * chamar session.commit() ou session.rollback()
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		/*
		 * Sub Interfaces de Destination: Queue, TemporaryQueue, TemporaryTopic, Topic.
		 */
		Destination destination = (Destination) context.lookup("LOG");

		MessageProducer producer = session.createProducer(destination);

		Message message = session.createTextMessage("DEGUG | <id>12345</id>");
		producer.send(message, DeliveryMode.NON_PERSISTENT, 3, 5000);

		// Recebe apenas uma mensagem
//		Message message = consumer.receive();

		session.close();
		context.close();
		connection.close();
	}

}