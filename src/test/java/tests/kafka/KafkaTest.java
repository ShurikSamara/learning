package tests.kafka;

import clients.KafkaBrokerClient;
import clients.base.kafka.impl.KafkaImpl;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static config.PropsConfig.getProps;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Kafka Tests")
@Feature("Message Processing")
public class KafkaTest {

    private KafkaBrokerClient brokerClient;
    private final String kafkaUrl = getProps().kafkaCluster();

    @BeforeEach
    void setUp() {
        brokerClient = new KafkaBrokerClient(new KafkaImpl());
    }

    @Test
    @DisplayName("Send and receive message from Kafka")
    @Description("Test sends a message to Kafka topic and verifies it can be received")
    @Story("Basic Kafka Messaging")
    void testSendAndReceiveMessage() {
        // Arrange
        String topic = "test-topic";
        String key = UUID.randomUUID().toString();
        String message = "{\"id\":\"" + key + "\",\"message\":\"Test message\"}";
        List<Header> headers = List.of(
            new RecordHeader("requestId", key.getBytes())
        );

        // Act
        brokerClient.sendMessage(kafkaUrl, topic, key, headers, message);

        // Assert
        List<String> messages = brokerClient.getMessages(kafkaUrl, topic, key);
        assertFalse(messages.isEmpty(), "No messages received");
        assertTrue(messages.stream().anyMatch(msg -> msg.contains(key)), 
                "Message with key " + key + " not found");
    }

    @ParameterizedTest(name = "Send message with payload: {0}")
    @MethodSource("getKafkaTestData")
    @DisplayName("Send different message payloads to Kafka")
    @Description("Test sends different message payloads to Kafka topic and verifies they can be received")
    @Story("Parameterized Kafka Messaging")
    void testSendAndReceiveParameterizedMessages(String payload) {
        // Arrange
        String topic = "test-topic";
        String key = UUID.randomUUID().toString();
        List<Header> headers = List.of(
            new RecordHeader("requestId", key.getBytes())
        );

        // Act
        brokerClient.sendMessage(kafkaUrl, topic, key, headers, payload);

        // Assert
        List<String> messages = brokerClient.getMessages(kafkaUrl, topic, key);
        assertFalse(messages.isEmpty(), "No messages received");
        assertTrue(messages.stream().anyMatch(msg -> msg.equals(payload)), 
                "Message with payload not found");
    }

    /**
     * Provides test data for Kafka tests
     * @return Stream of test data
     */
    static Stream<String> getKafkaTestData() {
        return Stream.of(
            "{\"id\":\"1\",\"message\":\"Test message 1\"}",
            "{\"id\":\"2\",\"message\":\"Test message 2\"}",
            "{\"id\":\"3\",\"message\":\"Test message 3\"}"
        );
    }
}
