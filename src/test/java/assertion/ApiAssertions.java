package assertion;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

/**
 * Assertion class for API tests
 * Contains methods with @Step annotations for API test assertions
 */
public class ApiAssertions {

    /**
     * Assert that the response status code is as expected
     * 
     * @param response ValidatableResponse to assert
     * @param expectedStatusCode Expected status code
     * @return ValidatableResponse for chaining
     */
    @Step("Assert that status code is {expectedStatusCode}")
    public ValidatableResponse assertStatusCode(ValidatableResponse response, int expectedStatusCode) {
        return response.statusCode(expectedStatusCode);
    }

    /**
     * Assert that the response body field equals the expected value
     * 
     * @param response ValidatableResponse to assert
     * @param fieldPath JSON path to the field
     * @param expectedValue Expected value
     * @return ValidatableResponse for chaining
     */
    @Step("Assert that field {fieldPath} equals {expectedValue}")
    public ValidatableResponse assertBodyFieldEquals(ValidatableResponse response, String fieldPath, Object expectedValue) {
        return response.body(fieldPath, equalTo(expectedValue));
    }

    /**
     * Assert that the response body field is not null
     * 
     * @param response ValidatableResponse to assert
     * @param fieldPath JSON path to the field
     * @return ValidatableResponse for chaining
     */
    @Step("Assert that field {fieldPath} is not null")
    public ValidatableResponse assertBodyFieldNotNull(ValidatableResponse response, String fieldPath) {
        return response.body(fieldPath, notNullValue());
    }

    /**
     * Assert user details in response
     * 
     * @param response ValidatableResponse to assert
     * @param userId Expected user ID
     * @return ValidatableResponse for chaining
     */
    @Step("Assert user details for user ID {userId}")
    public ValidatableResponse assertUserDetails(ValidatableResponse response, int userId) {
        return response
            .statusCode(200)
            .body("id", equalTo(userId))
            .body("name", notNullValue())
            .body("email", notNullValue())
            .body("address", notNullValue());
    }

    /**
     * Assert post details in response
     * 
     * @param response ValidatableResponse to assert
     * @param postId Expected post ID (or null for new posts)
     * @param title Expected title
     * @param body Expected body
     * @param userId Expected user ID
     * @return ValidatableResponse for chaining
     */
    @Step("Assert post details with title {title}")
    public ValidatableResponse assertPostDetails(ValidatableResponse response, Integer postId, String title, String body, int userId) {
        ValidatableResponse assertion = response
            .body("title", equalTo(title))
            .body("body", equalTo(body))
            .body("userId", equalTo(userId));
        
        if (postId != null) {
            assertion = assertion.body("id", equalTo(postId));
        } else {
            assertion = assertion.body("id", notNullValue());
        }
        
        return assertion;
    }
}