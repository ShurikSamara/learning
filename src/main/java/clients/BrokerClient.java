package clients;

import clients.base.kafka.interfaces.IBroker;
import org.apache.kafka.common.header.Header;
import utils.ZipkinTracer;

import java.util.List;

public abstract class BrokerClient {

  private final IBroker broker;

  public BrokerClient(IBroker broker) {
    this.broker = broker;
  }

  public void sendMessage(String url, String topic, String key, List<Header> headers, String message) {
    String spanId = ZipkinTracer.startSpan("BrokerClient.sendMessage");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("topic", topic);
      ZipkinTracer.addTag("key", key);

      broker.sendMessage(url, topic, key, headers, message);
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }

  public List<String> getMessages(String url, String topic, String id) {
    String spanId = ZipkinTracer.startSpan("BrokerClient.getMessages");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("topic", topic);
      ZipkinTracer.addTag("id", id);

      return broker.getMessages(url, topic, id);
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }
}
