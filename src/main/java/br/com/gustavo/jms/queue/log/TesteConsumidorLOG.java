package br.com.gustavo.jms.queue.log;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidorLOG {

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
		 * O segundo parâmetro diz qual será a estrategia de confirmação de recebimento da mensagem
		 * CLIENT_ACKNOWLEDGE faz com que precisamos confirmar no código manualmente o recebimento
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		/*
		 * Sub Interfaces de Destination: Queue, TemporaryQueue, TemporaryTopic, Topic.
		 */
		Destination destination = (Destination) context.lookup("LOG");
		MessageConsumer consumer = session.createConsumer(destination);

		consumer.setMessageListener(new MessageListener() {

			public void onMessage(Message message) {
				
				try {
					//Este metodo confirma o recebimento da mensagem
					System.out.println(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		
		new Scanner(System.in).nextLine();
		
		// Recebe apenas uma mensagem
//		Message message = consumer.receive();

		session.close();
		context.close();
		connection.close();
	}

}
