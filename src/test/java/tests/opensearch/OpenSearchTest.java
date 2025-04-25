package tests.opensearch;

import clients.OpenSearchClient;
import clients.base.opensearch.impl.OpenSearchImpl;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Epic("OpenSearch Tests")
@Feature("Document Management")
public class OpenSearchTest {

    private OpenSearchClient openSearchClient;
    private final String testIndex = "test-index";
    
    @BeforeEach
    void setUp() {
        openSearchClient = new OpenSearchClient(new OpenSearchImpl());
    }
    
    @Test
    @DisplayName("Index and retrieve document from OpenSearch")
    @Description("Test indexes a document in OpenSearch and verifies it can be retrieved")
    @Story("Basic OpenSearch Operations")
    void testIndexAndGetDocument() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String document = "{\"id\":\"" + id + "\",\"message\":\"Test message\"}";
        
        // Act
        boolean indexResult = openSearchClient.indexDocument(testIndex, id, document);
        
        // Assert
        assertTrue(indexResult, "Document indexing failed");
        
        // Act
        String retrievedDocument = openSearchClient.getDocument(testIndex, id);
        
        // Assert
        assertNotNull(retrievedDocument, "Retrieved document is null");
        assertTrue(retrievedDocument.contains(id), "Retrieved document does not contain the expected ID");
    }
    
    @Test
    @DisplayName("Search documents in OpenSearch")
    @Description("Test searches for documents in OpenSearch using a query")
    @Story("OpenSearch Query Operations")
    void testSearchDocuments() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String document = "{\"id\":\"" + id + "\",\"message\":\"Searchable test message\"}";
        
        // Act - Index a document first
        openSearchClient.indexDocument(testIndex, id, document);
        
        // Create a simple term query to find the document
        String query = "{\"term\":{\"id\":\"" + id + "\"}}";
        
        // Act - Search for the document
        List<String> searchResults = openSearchClient.searchDocuments(testIndex, query);
        
        // Assert
        assertFalse(searchResults.isEmpty(), "No search results found");
        assertTrue(searchResults.stream().anyMatch(doc -> doc.contains(id)), 
                "Search results do not contain the expected document");
    }
    
    @Test
    @DisplayName("Delete document from OpenSearch")
    @Description("Test deletes a document from OpenSearch and verifies it's no longer available")
    @Story("OpenSearch Document Lifecycle")
    void testDeleteDocument() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String document = "{\"id\":\"" + id + "\",\"message\":\"Document to be deleted\"}";
        
        // Act - Index a document first
        openSearchClient.indexDocument(testIndex, id, document);
        
        // Act - Delete the document
        boolean deleteResult = openSearchClient.deleteDocument(testIndex, id);
        
        // Assert
        assertTrue(deleteResult, "Document deletion failed");
        
        // Act - Try to retrieve the deleted document
        String retrievedDocument = openSearchClient.getDocument(testIndex, id);
        
        // Assert
        assertNull(retrievedDocument, "Document was not properly deleted");
    }
    
    @ParameterizedTest(name = "Index document with payload: {0}")
    @MethodSource("getOpenSearchTestData")
    @DisplayName("Index different document payloads to OpenSearch")
    @Description("Test indexes different document payloads to OpenSearch and verifies they can be retrieved")
    @Story("Parameterized OpenSearch Operations")
    void testIndexAndGetParameterizedDocuments(String document) {
        // Arrange
        String id = UUID.randomUUID().toString();
        
        // Act
        boolean indexResult = openSearchClient.indexDocument(testIndex, id, document);
        
        // Assert
        assertTrue(indexResult, "Document indexing failed");
        
        // Act
        String retrievedDocument = openSearchClient.getDocument(testIndex, id);
        
        // Assert
        assertNotNull(retrievedDocument, "Retrieved document is null");
        assertEquals(document, retrievedDocument, "Retrieved document does not match the indexed document");
    }
    
    /**
     * Provides test data for OpenSearch tests
     * @return Stream of test data
     */
    static Stream<String> getOpenSearchTestData() {
        return Stream.of(
            "{\"id\":\"1\",\"message\":\"Test message 1\"}",
            "{\"id\":\"2\",\"message\":\"Test message 2\",\"tags\":[\"important\",\"urgent\"]}",
            "{\"id\":\"3\",\"message\":\"Test message 3\",\"metadata\":{\"author\":\"Test User\",\"date\":\"2023-06-01\"}}"
        );
    }
}