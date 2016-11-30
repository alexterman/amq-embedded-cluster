package amq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.util.Date;

public class SimpleSuccessfulConsumer implements MessageListener, ExceptionListener{

    private String name;
    private String id;


    public SimpleSuccessfulConsumer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        SimpleSuccessfulConsumer simpleCrashingConsumer =
                new SimpleSuccessfulConsumer("simple-success", "simple-success-id");
        simpleCrashingConsumer.getConnected();
    }

    private void getConnected() throws JMSException {
        String url = "tcp://localhost:61616";


        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connectionFactory.setRedeliveryPolicy(getRedeliveryPolicy());

        Destination destination = new ActiveMQQueue("TEST.FOO");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(this);
    }

    private RedeliveryPolicy getRedeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setInitialRedeliveryDelay(5000);
        redeliveryPolicy.setRedeliveryDelay(2000);
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setUseExponentialBackOff(true);
        return redeliveryPolicy;
    }

    public void onException(JMSException exception) {
        exception.printStackTrace();
    }

    public void onMessage(Message message) {

        try {
            System.err.println("ID="+message.getJMSMessageID() + new Date(System.currentTimeMillis()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
