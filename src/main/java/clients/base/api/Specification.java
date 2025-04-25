package clients.base.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static config.PropsConfig.getProps;

public abstract class Specification {

  private static final String KEYSTORE = getProps().apiKeystore();
  private static final String TRUSTSTORE = getProps().apiTruststore();
  private static final String KEYSTORE_PSW = getProps().apiKeystorePassword();
  private static final String TRUSTSTORE_PSW = getProps().apiTruststorePassword();

  public RequestSpecification requestSpecification() {
    return new RequestSpecBuilder()
      .setKeyStore(KEYSTORE, KEYSTORE_PSW)
      .setTrustStore(TRUSTSTORE, TRUSTSTORE_PSW)
      .build();
  }

  public ResponseSpecification responseSpecification() {
    return new ResponseSpecBuilder()
      .build();
  }

  /** Метод для инсталяции спецификаций в клиенты сервисов
   * @param requestSpec  - спецификация описывающая паттерн вызова
   * @param responseSpec - спецификация описывающая паттерн ответа
   */
  public void installSpecification(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
    RestAssured.requestSpecification = requestSpec;
    RestAssured.responseSpecification = responseSpec;
  }
}