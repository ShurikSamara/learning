package data_provider;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DataProvider {
  /**
   * DataProvider - итерирует данные из csv файла.
   * @param filter - наименование топика.
   * @param pathToFile - путь до csv файла содержащий тестовые данные.
   * @return - тестовые данные.
   */
  @SneakyThrows
  public static Stream<Object[]> dataProviderFromCsvFile(String pathToFile, String filter) {
    List<Object[]> testParameters = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(pathToFile, StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        if(Objects.equals(filter, "all") && !line.startsWith("//") || filter.isEmpty() && !line.startsWith("//")) {
          String[] data = line.split(",");
          testParameters.add(data);
        }
        else if (line.contains(filter) && !line.contains("all") && !line.startsWith("//")) {
          String[] data = line.split(",");
          testParameters.add(data);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Не удалось прочитать файл: " + pathToFile, e);
    }
    return testParameters.stream();
  }

  @SneakyThrows
  public static Stream<Object[]> dataProviderFromCsvFile(String pathToFile) {
    List<Object[]> testParameters = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(pathToFile, StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        if(!line.startsWith("//")) {
          String[] data = line.split(",");
          testParameters.add(data);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Не удалось прочитать файл: " + pathToFile, e);
    }
    return testParameters.stream();
  }
}