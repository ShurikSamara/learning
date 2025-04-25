package clients.base.kafka.interfaces;

import org.apache.kafka.common.header.Header;

import java.util.List;

public interface IBroker {

  void sendMessage(String url, String topic, String key, List<Header> headers, String message);

  List<String> getMessages(String url, String topic, String requestId);
}