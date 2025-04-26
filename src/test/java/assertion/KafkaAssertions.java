package assertion;

import io.qameta.allure.Step;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Assertion class for Kafka tests
 * Contains methods with @Step annotations for Kafka test assertions
 */
public class KafkaAssertions {

    /**
     * Assert that messages were received from Kafka topic
     * 
     * @param messages List of received messages
     * @param topic Kafka topic
     */
    @Step("Assert messages were received from topic {topic}")
    public void assertMessagesReceived(List<String> messages, String topic) {
        assertFalse(messages.isEmpty(), "No messages received from topic: " + topic);
    }

    /**
     * Assert that a message with a specific key was received
     * 
     * @param messages List of received messages
     * @param key Message key
     * @param topic Kafka topic
     */
    @Step("Assert message with key {key} was received from topic {topic}")
    public void assertMessageWithKeyReceived(List<String> messages, String key, String topic) {
        assertFalse(messages.isEmpty(), "No messages received from topic: " + topic);
        assertTrue(messages.stream().anyMatch(msg -> msg.contains(key)), 
                "Message with key " + key + " not found in topic: " + topic);
    }

    /**
     * Assert that a message with a specific payload was received
     * 
     * @param messages List of received messages
     * @param payload Expected message payload
     * @param topic Kafka topic
     */
    @Step("Assert message with payload was received from topic {topic}")
    public void assertMessageWithPayloadReceived(List<String> messages, String payload, String topic) {
        assertFalse(messages.isEmpty(), "No messages received from topic: " + topic);
        assertTrue(messages.stream().anyMatch(msg -> msg.equals(payload)), 
                "Message with expected payload not found in topic: " + topic);
    }

    /**
     * Assert that a message with specific content was received
     * 
     * @param messages List of received messages
     * @param contentToMatch Content that should be in the message
     * @param topic Kafka topic
     */
    @Step("Assert message containing {contentToMatch} was received from topic {topic}")
    public void assertMessageContains(List<String> messages, String contentToMatch, String topic) {
        assertFalse(messages.isEmpty(), "No messages received from topic: " + topic);
        assertTrue(messages.stream().anyMatch(msg -> msg.contains(contentToMatch)), 
                "Message containing '" + contentToMatch + "' not found in topic: " + topic);
    }
}