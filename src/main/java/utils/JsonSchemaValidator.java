package utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for validating JSON against JSON Schema.
 * This implementation provides basic JSON syntax validation.
 * For full schema validation, consider using a dedicated library.
 */
public class JsonSchemaValidator {

    private static final Gson gson = new Gson();

    /**
     * Validates a JSON string against a JSON Schema.
     * This implementation checks:
     * 1. If the schema file exists
     * 2. If the schema file is valid JSON
     * 3. If the JSON string is valid JSON
     *
     * @param schemaPath - path to the JSON Schema file
     * @param jsonString - JSON string to validate
     * @throws JsonValidationException if validation fails or an error occurs
     */
    public static void validate(String schemaPath, String jsonString) {
        try {
            // Check if schema file exists
            File schemaFile = new File(schemaPath);
            if (!schemaFile.exists()) {
                System.out.println("Error: Schema file not found: " + schemaPath);
                throw new JsonValidationException("Schema file not found: " + schemaPath);
            }

            // Read schema file
            String schemaContent = new String(Files.readAllBytes(Paths.get(schemaPath)));

            // Validate schema syntax
            try {
                JsonElement schemaElement = JsonParser.parseString(schemaContent);
                System.out.println("Debug: Schema is valid JSON");
            } catch (JsonSyntaxException e) {
                System.out.println("Error: Schema is not valid JSON: " + e.getMessage());
                throw new JsonValidationException("Schema is not valid JSON", e);
            }

            // Validate JSON syntax
            try {
                JsonElement jsonElement = JsonParser.parseString(jsonString);
                System.out.println("Info: JSON validation passed (syntax check only)");
            } catch (JsonSyntaxException e) {
                System.out.println("Error: JSON is not valid: " + e.getMessage());
                throw new JsonValidationException("JSON is not valid", e);
            }

            // Note: This implementation only validates JSON syntax.
            // For full schema validation, a dedicated library should be used.
            System.out.println("Warning: This implementation only validates JSON syntax, not schema compliance");

        } catch (IOException e) {
            System.out.println("Error: Error reading schema file: " + e.getMessage());
            throw new JsonValidationException("Error reading schema file", e);
        } catch (Exception e) {
            if (!(e instanceof JsonValidationException)) {
                System.out.println("Error: Unexpected error during validation: " + e.getMessage());
                throw new JsonValidationException("Unexpected error during validation", e);
            } else {
                throw e;
            }
        }
    }

    /**
     * For backward compatibility with existing code.
     * 
     * @param jsonSchema - path to the JSON Schema file
     * @param json - JSON string to validate
     * @throws JsonValidationException if validation fails or an error occurs
     */
    public static void jsonSchemaValidator(String jsonSchema, String json) {
        validate(jsonSchema, json);
    }
}
