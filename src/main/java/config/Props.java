package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
  "system:env",
  "system:properties",
  "classpath:properties/${env}.properties"
})
public interface Props extends Config {

  /** CONFIGURATIONS */
  @DefaultValue("ift")
  @Key("ENV")
  String env();

  @Key("LOG_LEVEL")
  String logLevel();

  @DefaultValue("900000")
  @Key("TEST_CONFIG_TIMEOUT")
  long testConfigTimeOut();

  @DefaultValue("60")
  @Key("CONFIG_RETRY_TIMEOUT")
  long configRetryTimeout();

  @DefaultValue("all")
  @Key("DATA_PROVIDER_FILTER_KEY")
  String dataProviderFilterKey();


  /** PATH_TO_DIRECTORIES */
  @Key("PATH_TO_TEST_DATA")
  String pathToTestData();

  @Key("PATH_TO_JSON_SCHEMA")
  String pathToJsonSchema();

  @Key("PATH_TEST_RESOURCES")
  String pathToTestResources();


  /** KAFKA */
  @Key("KAFKA_KEYSTORE")
  String kafkaKeystore();

  @Key("KAFKA_TRUSTSTORE")
  String kafkaTruststore();

  @Key("KAFKA_KEYSTORE_PASSWORD")
  String kafkaKeystorePassword();

  @Key("KAFKA_TRUSTSTORE_PASSWORD")
  String kafkaTruststorePassword();

  @Key("CONSUMER_POOL_DURATION")
  long consumerPoolDuration();

  @Key("KAFKA_MAX_MESSAGES_COUNT")
  int kafkaMaxMessagesValue();

  @Key("PRODUCER_RECORDS_TIMEOUT")
  long producerRecordTimeout();

  @Key("KAFKA_CLUSTER")
  String kafkaCluster();


  /** DATABASE */
  @Key("DB_URL")
  String dbUrl();

  @Key("DB_USERNAME")
  String dbUsername();

  @Key("DB_PASSWORD")
  String dbPassword();


  /** API */
  @Key("API_KEYSTORE")
  String apiKeystore();

  @Key("API_TRUSTSTORE")
  String apiTruststore();

  @Key("API_KEYSTORE_PASSWORD")
  String apiKeystorePassword();

  @Key("API_TRUSTSTORE_PASSWORD")
  String apiTruststorePassword();


  /** OPENSEARCH */
  @Key("OPENSEARCH_URL")
  String openSearchUrl();

  @Key("OPENSEARCH_USERNAME")
  String openSearchUsername();

  @Key("OPENSEARCH_PASSWORD")
  String openSearchPassword();


  /** ZIPKIN */
  @DefaultValue("true")
  @Key("ZIPKIN_ENABLED")
  boolean zipkinEnabled();

  @DefaultValue("http://localhost:9411/api/v2/spans")
  @Key("ZIPKIN_URL")
  String zipkinUrl();


  /** SERVICE_CREDENTIALS */
  //base
  @Key("USER_LOGIN")
  String userLogin();

  @Key("USER_PASSWORD")
  String userPassword();
}
