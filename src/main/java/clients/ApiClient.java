package clients;

import clients.base.api.Specification;
import io.restassured.response.ValidatableResponse;
import utils.ZipkinTracer;

import java.util.Map;

import static io.restassured.RestAssured.given;

 public class ApiClient extends Specification {
  /**
   * [GET]
   * @param url адрес сервиса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendGet(
    String url,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams,
    Map<String, String> cookies
    ) {
    String spanId = ZipkinTracer.startSpan("ApiClient.sendGet");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("method", "GET");

      installSpecification(requestSpecification(), responseSpecification());
      return given()
        .redirects().follow(false)
        .headers(headers)
        .log().cookies()
        .log().headers()
        .cookies(cookies)
        .pathParams(pathParams)
        .queryParams(queryParams)
        .when()
        .get(url)
        .then()
        .log().all();
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }

  /**
   * [POST]
   * @param url адрес сервиса
   * @param statusCode ожидаемый статус код
   * @param body тело запроса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendPost(
    String url,
    int statusCode,
    String body,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    String spanId = ZipkinTracer.startSpan("ApiClient.sendPost");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("method", "POST");
      ZipkinTracer.addTag("statusCode", String.valueOf(statusCode));

      installSpecification(requestSpecification(), responseSpecification());
      return given()
        .redirects().follow(false)
        .headers(headers)
        .log().cookies()
        .log().headers()
        .pathParams(pathParams)
        .queryParams(queryParams)
        .body(body)
        .when()
        .post(url)
        .then()
        .assertThat().statusCode(statusCode)
        .log().all();
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }

  /**
   * [PUT]
   * @param url адрес сервиса
   * @param statusCode ожидаемый статус код
   * @param body тело запроса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendPut(
    String url,
    int statusCode,
    String body,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    String spanId = ZipkinTracer.startSpan("ApiClient.sendPut");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("method", "PUT");
      ZipkinTracer.addTag("statusCode", String.valueOf(statusCode));

      installSpecification(requestSpecification(), responseSpecification());
      return given()
        .headers(headers)
        .pathParams(pathParams)
        .queryParams(queryParams)
        .body(body)
        .when()
        .put(url)
        .then()
        .assertThat().statusCode(statusCode)
        .log().ifError();
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }

  /**
   * [PATCH]
   * @param url адрес сервиса
   * @param statusCode ожидаемый статус код
   * @param body тело запроса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendPatch(
    String url,
    int statusCode,
    String body,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    String spanId = ZipkinTracer.startSpan("ApiClient.sendPatch");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("method", "PATCH");
      ZipkinTracer.addTag("statusCode", String.valueOf(statusCode));

      installSpecification(requestSpecification(), responseSpecification());
      return given()
        .headers(headers)
        .pathParams(pathParams)
        .queryParams(queryParams)
        .body(body)
        .when()
        .patch(url)
        .then()
        .assertThat().statusCode(statusCode)
        .log().ifError();
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }

  /**
   * [DELETE]
   * @param url         адрес сервиса
   * @param statusCode  ожидаемый статус код
   * @param headers     заголовки
   * @param pathParams  параметры пути запроса
   * @param queryParams параметры запроса
   */
  public void sendDelete(
    String url,
    int statusCode,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    String spanId = ZipkinTracer.startSpan("ApiClient.sendDelete");
    try {
      ZipkinTracer.addTag("url", url);
      ZipkinTracer.addTag("method", "DELETE");
      ZipkinTracer.addTag("statusCode", String.valueOf(statusCode));

      installSpecification(requestSpecification(), responseSpecification());
      given()
        .headers(headers)
        .pathParams(pathParams)
        .queryParams(queryParams)
        .when()
        .delete(url)
        .then()
        .assertThat().statusCode(statusCode)
        .log().ifError();
    } finally {
      ZipkinTracer.endSpan(spanId);
    }
  }


  //ADVANCED_CLIENTS

  /**
   * [GET] с авторизацией
   * @param url адрес сервиса
   * @param login login
   * @param password password
   * @param statusCode ожидаемый статус код
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendGetWithLoginAndPassword(
    String url,
    int statusCode,
    String login,
    String password,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .auth()
      .basic(login, password)
      .headers(headers)
      .pathParams(pathParams)
      .queryParams(queryParams)
      .when()
      .get(url)
      .then()
      .assertThat().statusCode(statusCode)
      .log().ifError();
  }

  /**
   * [GET] c типом URL_ENCODED
   * @param url адрес сервиса
   * @param headers заголовки
   * @param formParams тело запроса
   * @param cookies cookies
   */
  public ValidatableResponse sendGetWithFormParamsAndCookies(
    String url,
    Map<String, String> headers,
    Map<String, String> cookies,
    Map<String, String> formParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .cookies(cookies)
      .headers(headers)
      .formParams(formParams)
      .when()
      .get(url)
      .then();
  }

  /**
   * [POST] c авторизацией
   * @param url адрес сервиса
   * @param statusCode ожидаемый статус код
   * @param login login
   * @param password password
   * @param body тело запроса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendPostWithLoginAndPassword(
    String url,
    int statusCode,
    String login,
    String password,
    String body,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .auth()
      .basic(login, password)
      .headers(headers)
      .pathParams(pathParams)
      .queryParams(queryParams)
      .body(body)
      .when()
      .post(url)
      .then()
      .assertThat().statusCode(statusCode)
      .log().ifError();
  }

  /**
   * [POST] c типом URL_ENCODED
   * @param url адрес сервиса
   * @param headers заголовки
   * @param formParams тело запроса
   */
  public ValidatableResponse sendPostWithFormParams(
    String url,
    Map<String, String> headers,
    Map<String, String> formParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .headers(headers)
      .formParams(formParams)
      .when()
      .post(url)
      .then();
  }

  /**
   * [POST] c типом URL_ENCODED
   * @param url адрес сервиса
   * @param headers заголовки
   * @param formParams тело запроса
   * @param cookies cookies
   */
  public ValidatableResponse sendPostWithFormParamsAndCookies(
    String url,
    Map<String, String> headers,
    Map<String, String> cookies,
    Map<String, String> formParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .cookies(cookies)
      .headers(headers)
      .formParams(formParams)
      .when()
      .post(url)
      .then();
  }

   /**
    * Метод для отправки POST запросов c типом URL_ENCODED
    * @param url - адресс сервиса
    * @param headers - заголовки
    * @param formParams - тело запроса
    * @param cookies - cookies
    */
   public ValidatableResponse sendPostUrlEncodedClient(
     String url,
     Map<String, String> headers,
     Map<String, String> formParams,
     Map<String, String> cookies
   ) {
     installSpecification(requestSpecification(), responseSpecification());
     return given()
       .redirects().follow(false)
       .cookies(cookies)
       .headers(headers)
       .formParams(formParams)
       .when()
       .post(url)
       .then().log().all();
   }

  /**
   * [PUT] c авторизацией
   * @param url адрес сервиса
   * @param statusCode ожидаемый статус код
   * @param login login
   * @param password password
   * @param body тело запроса
   * @param headers заголовки
   * @param pathParams параметры пути запроса
   * @param queryParams параметры запроса
   */
  public ValidatableResponse sendPutWithLoginAndPassword(
    String url,
    int statusCode,
    String login,
    String password,
    String body,
    Map<String, String> headers,
    Map<String, String> pathParams,
    Map<String, String> queryParams
  ) {
    installSpecification(requestSpecification(), responseSpecification());
    return given()
      .auth()
      .basic(login, password)
      .headers(headers)
      .pathParams(pathParams)
      .queryParams(queryParams)
      .body(body)
      .when()
      .put(url)
      .then()
      .assertThat().statusCode(statusCode)
      .log().ifError();
  }
}
