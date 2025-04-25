package clients;

import clients.base.kafka.interfaces.IBroker;

/**
 * Concrete implementation of BrokerClient for Kafka
 */
public class KafkaBrokerClient extends BrokerClient {
    
    /**
     * Constructor with Kafka broker implementation
     * 
     * @param broker Kafka broker implementation
     */
    public KafkaBrokerClient(IBroker broker) {
        super(broker);
    }
}