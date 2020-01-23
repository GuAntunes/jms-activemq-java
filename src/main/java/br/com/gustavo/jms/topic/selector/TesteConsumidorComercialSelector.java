package br.com.gustavo.jms.topic.selector;

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
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteConsumidorComercialSelector {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		InitialContext context = new InitialContext();

		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//Define o client id que será identificado pelo tópico
		connection.setClientID("comercial");
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
		Topic destination = (Topic) context.lookup("loja");
		//Cria uma assinatura duravel
		//O selector permite que seja realizado um filtro de escolha para receber a mensagem
		//No caso este consumidor irá receber a mensagem caso a propriedade ebook seja false
		//Ou não seja definido um parâmetro ebook is null
		//O ultimo parametro deste metodo diz respeito se eu quero ou não receber mensagens produzidas por esta mesma conexão
		MessageConsumer consumer = session.createDurableSubscriber( destination, "assinatura-selector","ebook is null OR ebook=false",false);

		consumer.setMessageListener(new MessageListener() {

			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				
				try {
					System.out.println("Recebendo a mensagem: " + textMessage.getText());
				} catch (JMSException e) {
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
