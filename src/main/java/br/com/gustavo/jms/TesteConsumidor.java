package br.com.gustavo.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteConsumidor {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		InitialContext context = new InitialContext();
		
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		/* O primeiro parâmetro do método createSession define se queremos usar o tratamento da 
		 * transação como explícito. Como colocamos false, não é preciso chamar session.commit() ou session.rollback()
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		/* Sub Interfaces de Destination:
		 * Queue, TemporaryQueue, TemporaryTopic, Topic.
		 */
		Destination destination  = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(destination);
		
		Message message = consumer.receive();
		
		System.out.println("Recebendo a mensagem: " + message);
		
		session.close();
		context.close();
		connection.close();
	}

}
