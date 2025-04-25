package tests.api;

import clients.ApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("API Tests")
@Feature("REST API Operations")
public class ApiTest {

    private ApiClient apiClient;
    private final String baseUrl = "https://jsonplaceholder.typicode.com";
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
    }
    
    @Test
    @DisplayName("Get user by ID")
    @Description("Test retrieves a user by ID and verifies the response")
    @Story("User API")
    void testGetUserById() {
        // Arrange
        String url = baseUrl + "/users/{id}";
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "1");
        
        // Act
        ValidatableResponse response = apiClient.sendGet(
            url,
            new HashMap<>(),  // headers
            pathParams,
            new HashMap<>(),  // queryParams
            new HashMap<>()   // cookies
        );
        
        // Assert
        response
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue())
            .body("address", notNullValue());
    }
    
    @Test
    @DisplayName("Create a new post")
    @Description("Test creates a new post and verifies the response")
    @Story("Post API")
    void testCreatePost() {
        // Arrange
        String url = baseUrl + "/posts";
        String body = "{\n" +
                      "  \"title\": \"Test Post\",\n" +
                      "  \"body\": \"This is a test post\",\n" +
                      "  \"userId\": 1\n" +
                      "}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        // Act
        ValidatableResponse response = apiClient.sendPost(
            url,
            201,
            body,
            headers,
            new HashMap<>(),  // pathParams
            new HashMap<>()   // queryParams
        );
        
        // Assert
        response
            .body("title", equalTo("Test Post"))
            .body("body", equalTo("This is a test post"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }
    
    @Test
    @DisplayName("Update a post")
    @Description("Test updates an existing post and verifies the response")
    @Story("Post API")
    void testUpdatePost() {
        // Arrange
        String url = baseUrl + "/posts/{id}";
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "1");
        String body = "{\n" +
                      "  \"title\": \"Updated Post\",\n" +
                      "  \"body\": \"This post has been updated\",\n" +
                      "  \"userId\": 1\n" +
                      "}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        // Act
        ValidatableResponse response = apiClient.sendPut(
            url,
            200,
            body,
            headers,
            pathParams,
            new HashMap<>()  // queryParams
        );
        
        // Assert
        response
            .body("title", equalTo("Updated Post"))
            .body("body", equalTo("This post has been updated"))
            .body("userId", equalTo(1))
            .body("id", equalTo(1));
    }
    
    @Test
    @DisplayName("Delete a post")
    @Description("Test deletes a post and verifies the response")
    @Story("Post API")
    void testDeletePost() {
        // Arrange
        String url = baseUrl + "/posts/{id}";
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "1");
        
        // Act
        apiClient.sendDelete(
            url,
            200,
            new HashMap<>(),  // headers
            pathParams,
            new HashMap<>()   // queryParams
        );
        
        // No explicit assertion needed as sendDelete will assert status code 200
    }
    
    @ParameterizedTest(name = "Get post with ID: {0}")
    @MethodSource("getPostIds")
    @DisplayName("Get posts by different IDs")
    @Description("Test retrieves posts with different IDs and verifies the responses")
    @Story("Parameterized API Testing")
    void testGetPostsByIds(int postId) {
        // Arrange
        String url = baseUrl + "/posts/{id}";
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", String.valueOf(postId));
        
        // Act
        ValidatableResponse response = apiClient.sendGet(
            url,
            new HashMap<>(),  // headers
            pathParams,
            new HashMap<>(),  // queryParams
            new HashMap<>()   // cookies
        );
        
        // Assert
        response
            .statusCode(200)
            .body("id", equalTo(postId))
            .body("userId", notNullValue())
            .body("title", notNullValue())
            .body("body", notNullValue());
    }
    
    /**
     * Provides test data for API tests
     * @return Stream of post IDs
     */
    static Stream<Integer> getPostIds() {
        return Stream.of(1, 2, 3, 4, 5);
    }
}