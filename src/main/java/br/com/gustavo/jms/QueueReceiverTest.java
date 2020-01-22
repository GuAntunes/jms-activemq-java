package br.com.gustavo.jms;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

/**
 * 
 * @author Gustavo Antunes
 * Criação de um consumidor especifico para uma fila
 */
public class QueueReceiverTest {

	public static void main(String[] args) throws Exception {
		
		//Inicia o contexto JMS configurado na aplicação
		//No caso estamos utilizando o arquivo jndi.properties
		InitialContext context = new InitialContext();
		
		//Criação da fabrica a partir do context
		QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		//Criação da conexão a partir da fábrica de conexões
		QueueConnection connection = factory.createQueueConnection();
		//Inicia a conexão
		connection.start();
		
		/* O primeiro parâmetro do método createSession define se queremos usar o tratamento da 
		 * transação como explícito. Como colocamos false, não é preciso chamar session.commit() ou session.rollback()
		 */
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		//Aponta para a fila desejada
		Queue queue = (Queue) context.lookup("financeiro");
		
		//Cria um objeto Receiver capaz de receber efetivamente a mensagem da fila apontada
		QueueReceiver receiver = session.createReceiver(queue);
		
		//Chama o método para reber uma mensagem da fila
		Message message = receiver.receive();
		
		System.out.println(message);
		
		
		connection.close();
		session.close();
		context.close();
	}
	
}
