package clients.base.kafka.impl;

import clients.base.kafka.interfaces.IBroker;
import org.apache.kafka.common.header.Header;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static config.PropsConfig.getProps;

@Slf4j
public class KafkaImpl implements IBroker {

  private final String KEYSTORE = getProps().kafkaKeystore();
  private final String TRUSTSTORE = getProps().kafkaTruststore();
  private final String KEYSTORE_PSW = getProps().kafkaKeystorePassword();
  private final String TRUSTSTORE_PSW = getProps().kafkaTruststorePassword();

  private final long CONSUMER_POOL_DURATION = getProps().consumerPoolDuration();
  private final long PRODUCER_RECORD_TIMEOUT = getProps().producerRecordTimeout();
  private final int KAFKA_MAX_MESSAGES_VALUE = getProps().kafkaMaxMessagesValue();

  @Override
  public void sendMessage(String url, String topic, String key, List<Header> headers, String message) {

  }

  @Override
  public List<String> getMessages(String url, String topic, String requestId) {
    return List.of();
  }
}