package amq;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

/**
 * Created by alex on 11/30/16.
 */
public class SimpleBroker {

    public static void main(String[] args) throws Exception {


        BrokerService broker = BrokerFactory.createBroker(new URI("xbean:activemq.xml"));
        broker.setUseJmx(false);
        broker.start();
    }

}
