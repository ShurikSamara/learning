# Проект автоматизированного тестирования для Kafka, OpenSearch и REST API

Этот проект предоставляет фреймворк для автоматизированного тестирования сервисов Kafka, OpenSearch и REST API. Он включает в себя клиентские реализации для взаимодействия с этими сервисами и примеры тестовых классов, демонстрирующих, как писать тесты для каждой технологии.

Проект поддерживает запуск в контейнерах Docker, а также в средах Kubernetes, OpenShift и Jenkins.

## Структура проекта

Проект следует модульной структуре с четким разделением ответственности:

- **Клиенты**: Предоставляют высокоуровневые API для взаимодействия с сервисами
  - `ApiClient`: Для взаимодействия с REST API
  - `KafkaBrokerClient`: Для взаимодействия с Kafka
  - `OpenSearchClient`: Для взаимодействия с OpenSearch

- **Базовые реализации**: Предоставляют низкоуровневые реализации взаимодействия с сервисами
  - `KafkaImpl`: Реализует операции Kafka
  - `OpenSearchImpl`: Реализует операции OpenSearch
  - `PostgresImpl`: Реализует операции с базой данных

- **Тестовые классы**: Демонстрируют, как писать тесты для каждой технологии
  - `KafkaTest`: Тесты для операций Kafka
  - `OpenSearchTest`: Тесты для операций OpenSearch
  - `ApiTest`: Тесты для операций REST API

## Конфигурация

Конфигурация управляется через файлы свойств и интерфейс `Props`:

1. Отредактируйте `src/main/resources/properties/application.properties` для установки конфигурации, специфичной для вашей среды
2. Интерфейс `Props` в `src/main/java/config/Props.java` определяет доступные свойства конфигурации
3. Используйте `PropsConfig.getProps()` для доступа к свойствам конфигурации в вашем коде

## Запуск тестов

### Предварительные требования

- Java 21 или выше
- Maven
- Доступ к сервисам Kafka, OpenSearch и REST API

### Запуск всех тестов

```bash
mvn clean test
```

### Запуск конкретных тестовых классов

```bash
# Запуск тестов Kafka
mvn test -Dtest=KafkaTest

# Запуск тестов OpenSearch
mvn test -Dtest=OpenSearchTest

# Запуск тестов API
mvn test -Dtest=ApiTest
```

### Запуск тестового набора

```bash
mvn test -Dtest=SuiteRegress
```

## Расширение фреймворка

### Добавление новых тестовых классов

1. Создайте новый тестовый класс в соответствующем пакете (например, `tests.kafka`, `tests.opensearch`, `tests.api`)
2. Добавьте новый тестовый класс в тестовый набор в `src/test/java/suites/SuiteRegress.java`

### Добавление новых клиентских реализаций

1. Создайте новый интерфейс в соответствующем пакете (например, `clients.base.newservice.interfaces`)
2. Создайте реализацию интерфейса в соответствующем пакете (например, `clients.base.newservice.impl`)
3. Создайте клиентский класс, который использует реализацию (например, `clients.NewServiceClient`)
4. Добавьте свойства конфигурации в интерфейс `Props` и `application.properties`

## Запуск в контейнерах и облачных средах

### Docker

Проект включает Dockerfile и docker-compose.yml для запуска тестов в контейнерах Docker.

#### Сборка Docker-образа

```bash
docker build -t test-app .
```

#### Запуск с помощью Docker Compose

Docker Compose настраивает все необходимые сервисы (PostgreSQL, Kafka, OpenSearch) и запускает тесты:

```bash
docker-compose up
```

Для запуска только тестов (если сервисы уже запущены):

```bash
`docker-compose up test-app`
```

### Kubernetes/OpenShift

Проект включает конфигурационные файлы для запуска в Kubernetes или OpenShift.

#### Запуск как Job

Для однократного запуска тестов:

```bash
# Замените ${REGISTRY} на путь к вашему реестру Docker
kubectl apply -f kubernetes/deployment.yaml
```

#### Запуск как CronJob

Для периодического запуска тестов:

```bash
# Замените ${REGISTRY} на путь к вашему реестру Docker
kubectl apply -f kubernetes/cronjob.yaml
```

### Jenkins

Проект включает Jenkinsfile для запуска тестов в Jenkins Pipeline:

1. Создайте новый Pipeline в Jenkins
2. Настройте Pipeline для использования SCM и укажите путь к Jenkinsfile
3. Создайте учетные данные в Jenkins для доступа к базе данных и OpenSearch:
   - `db-credentials`: Учетные данные для доступа к PostgreSQL
   - `opensearch-credentials`: Учетные данные для доступа к OpenSearch
4. Запустите Pipeline

## Используемые технологии

- **JUnit 5**: Тестовый фреймворк
- **Allure**: Отчетность
- **RestAssured**: Тестирование REST API
- **Kafka Client**: Взаимодействие с Kafka
- **OpenSearch Client**: Взаимодействие с OpenSearch
- **PostgreSQL JDBC**: Взаимодействие с базой данных
- **Owner**: Управление конфигурацией
- **Logback**: Логирование
- **Docker**: Контейнеризация
- **Kubernetes/OpenShift**: Оркестрация контейнеров
- **Jenkins**: CI/CD
