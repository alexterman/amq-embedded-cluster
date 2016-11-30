package amq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

/**
 * Created by alex on 11/30/16.
 */
public class SimpleConsumer implements MessageListener, ExceptionListener{

    private String name;
    private String id;


    public SimpleConsumer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static void main(String[] args) throws JMSException, InterruptedException {


        SimpleConsumer simpleConsumer = new SimpleConsumer("simple-name", "simple-id");

        simpleConsumer.getConnected();


    }

    private void getConnected() throws JMSException {
        String url = "tcp://localhost:61616";


        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Destination destination = new ActiveMQQueue("TEST.FOO");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(this);
    }

    public void onException(JMSException exception) {
        exception.printStackTrace();
    }

    public void onMessage(Message message) {

        try {
            System.err.println("ID="+message.getJMSMessageID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Crashing consumer");
    }
}
