package br.com.gustavo.jms;

import java.util.Enumeration;

import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

/**
 * 
 * @author Gustavo Antunes
 * Dependendo da nossa aplicação, podemos precisar apenas checar (monitoramento) 
 * as mensagens que chegaram para uma determinada fila sem consumi-la. Ou seja, 
 * apenas queremos ver sem tirá-las da fila. Para isso podemos usar um componente do JMS 
 * chamado QueueBrowser, usado para navegar sobre as mensagens sem consumi-las.
 */
public class BrowserConsumidor {

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
		QueueBrowser browser = session.createBrowser(queue);
		
		Enumeration msgs = browser.getEnumeration();
		while (msgs.hasMoreElements()) { 
		    TextMessage msg = (TextMessage) msgs.nextElement(); 
		    System.out.println("Message: " + msg.getText()); 
		}
		connection.close();
		session.close();
		context.close();
	}
	
}
