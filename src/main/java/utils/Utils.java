package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.awaitility.core.ConditionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static config.PropsConfig.getProps;
import static org.awaitility.Awaitility.await;

@Slf4j
public class Utils {

  /**
   * Метод 'awaitility' для установки ожидания
   */
  public static ConditionFactory getConditionFactory() {
    return await().atMost(getProps().configRetryTimeout(), TimeUnit.SECONDS);
  }

  /**
   * Метод для генерации уникального uuid
   */
  public static String uuid() {
    return UUID.randomUUID().toString();
  }

  /**
   * Метод для получения текущей даты
   */
  public static String getLocalDateTime(String pattern) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
  }

  public static String getRandomNumber(int length) {
    return new Faker().regexify("[1-9]{" + length + "}");
  }

  /**
   * Метод для преобразования объекта в формат JSON
   *
   * @param object - объект
   */
  public static String getSerialize(Object object) {
    return new GsonBuilder().setPrettyPrinting().create().toJson(object);
  }

  /**
   * Метод для преобразования JSON в модель
   *
   * @param json       - строка в формате JSON
   * @param modelClass - модель класса
   */
  public static <T> T getDeserialize(String json, Class<T> modelClass) {
    return new Gson().fromJson(json, modelClass);
  }

  /**
   * Метод для чтения файлов json
   *
   * @param jsonFilePath - путь до json файла
   */
  @SneakyThrows
  public static JsonElement readJsonFile(String jsonFilePath) {
    return JsonParser.parseReader(new FileReader(jsonFilePath));
  }

  /**
   * Метод для конвертирования строки в виде JSON в JsonObject
   *
   * @param json       - строка в формате JSON
   * @param removeKeys - список ключей, которые нужно удалить (необязательный)
   */
  public static JsonObject getJsonObject(String json, String... removeKeys) {
    JsonObject jsonObject = getDeserialize(json, JsonObject.class);
    for (String key : removeKeys) {
      jsonObject.remove(key);
    }
    return jsonObject;
  }

  public static JsonObject getJsonObject(String json) {
    return getDeserialize(json, JsonObject.class);
  }

  /**
   * Метод для конвертирования строки json в виде JSON в JsonArray
   *
   * @param json       - строка в формате json, вида массива объектов
   * @param removeKeys - список ключей, которые нужно удалить (необязательный)
   */
  public static JsonArray getJsonArray(String json, String... removeKeys) {
    JsonArray deserializeResponseList = getDeserialize(json, JsonArray.class);
    JsonArray jsonArray = new JsonArray();

    for (JsonElement obj : deserializeResponseList) {
      JsonObject jsonObject = obj.getAsJsonObject();
      for (String key : removeKeys) {
        jsonObject.remove(key);
      }
      JsonElement sortedObject = sortFieldsInJsonString(obj.toString());
      jsonArray.add(sortedObject);
    }

    return getJsonElements(jsonArray);
  }

  @NotNull
  private static JsonArray getJsonElements(JsonArray jsonArray) {
    List<JsonElement> jsonElementList = new ArrayList<>();
    jsonArray.forEach(jsonElementList::add);
    jsonElementList.sort(Comparator.comparing(JsonElement::toString));

    JsonArray jsonArrayList = new JsonArray();
    jsonElementList.forEach(jsonArrayList::add);
    return jsonArrayList;
  }

  public static JsonElement sortFieldsInJsonString(String jsonString) {
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    return sortFieldsInJsonElement(jsonElement);
  }

  private static JsonElement sortFieldsInJsonElement(JsonElement jsonElement) {
    if (jsonElement.isJsonObject()) {
      JsonObject sortedJsonObject = new JsonObject();
      TreeMap<String, JsonElement> sortedMap = new TreeMap<>();

      for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
        sortedMap.put(entry.getKey(), sortFieldsInJsonElement(entry.getValue()));
      }

      for (Map.Entry<String, JsonElement> entry : sortedMap.entrySet()) {
        sortedJsonObject.add(entry.getKey(), entry.getValue());
      }

      return sortedJsonObject;

    } else if (jsonElement.isJsonArray()) {
      JsonArray jsonArray = new JsonArray();
      for (JsonElement element : jsonElement.getAsJsonArray()) {
        jsonArray.add(sortFieldsInJsonElement(element));
      }

      return getJsonElements(jsonArray);
    }
    return jsonElement;
  }

  /**
   * Метод для извлечения ответа из json по jsonPath
   */
  public static JsonElement getValue(String json, String jsonPath) {
    try {
      return getJsonPath(json, jsonPath);
    } catch (Exception e) {
      log.error("json_path_in_json_does_not_exist: {}", jsonPath);
      return new JsonArray();
    }
  }

  /**
   * Метод для извлечения ответа из списка json по jsonPath
   */
  public static List<JsonElement> getValues(List<String> jsons, String jsonPath) {
    List<JsonElement> resultJson = new ArrayList<>();
    try {
      for (String json : jsons) {
        resultJson.add(getJsonPath(json, jsonPath));
      }
      return resultJson;
    } catch (Exception e) {
      log.error("json_path_does_not_exist: {}", jsonPath);
      return new ArrayList<>();
    }
  }

  public static JsonElement getJsonPath(String json, String jsonPath) {
    return getDeserialize(JsonPath.read(json, jsonPath).toString(), JsonElement.class);
  }

  /**
   * Метод для удаления файлов из директории проекта
   */
  public static void deleteFile(String pathToFile) {
    try {
      Files.delete(Path.of(pathToFile));
      log.info("Файл успешно удален: {}", pathToFile);
    } catch (IOException e) {
      log.error("Ошибка при удалении файла: {}", e.getMessage());
    }
  }
}