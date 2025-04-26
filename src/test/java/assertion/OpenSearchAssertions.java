package assertion;

import io.qameta.allure.Step;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assertion class for OpenSearch tests
 * Contains methods with @Step annotations for OpenSearch test assertions
 */
public class OpenSearchAssertions {

    /**
     * Assert that document indexing was successful
     * 
     * @param indexResult Result of indexing operation
     * @param documentId ID of the document
     */
    @Step("Assert document with ID {documentId} was indexed successfully")
    public void assertDocumentIndexed(boolean indexResult, String documentId) {
        assertTrue(indexResult, "Document indexing failed for ID: " + documentId);
    }

    /**
     * Assert that document retrieval was successful
     * 
     * @param retrievedDocument Retrieved document
     * @param documentId ID of the document
     */
    @Step("Assert document with ID {documentId} was retrieved successfully")
    public void assertDocumentRetrieved(String retrievedDocument, String documentId) {
        assertNotNull(retrievedDocument, "Retrieved document is null for ID: " + documentId);
        assertTrue(retrievedDocument.contains(documentId), 
                "Retrieved document does not contain the expected ID: " + documentId);
    }

    /**
     * Assert that document search returned results
     * 
     * @param searchResults List of search results
     * @param documentId ID of the document to find
     */
    @Step("Assert search results contain document with ID {documentId}")
    public void assertSearchResults(List<String> searchResults, String documentId) {
        assertFalse(searchResults.isEmpty(), "No search results found");
        assertTrue(searchResults.stream().anyMatch(doc -> doc.contains(documentId)), 
                "Search results do not contain the expected document with ID: " + documentId);
    }

    /**
     * Assert that document deletion was successful
     * 
     * @param deleteResult Result of deletion operation
     * @param documentId ID of the document
     */
    @Step("Assert document with ID {documentId} was deleted successfully")
    public void assertDocumentDeleted(boolean deleteResult, String documentId) {
        assertTrue(deleteResult, "Document deletion failed for ID: " + documentId);
    }

    /**
     * Assert that document is no longer available after deletion
     * 
     * @param retrievedDocument Retrieved document (should be null)
     * @param documentId ID of the document
     */
    @Step("Assert document with ID {documentId} is no longer available")
    public void assertDocumentNotAvailable(String retrievedDocument, String documentId) {
        assertNull(retrievedDocument, "Document with ID: " + documentId + " was not properly deleted");
    }

    /**
     * Assert that retrieved document matches expected document
     * 
     * @param retrievedDocument Retrieved document
     * @param expectedDocument Expected document
     * @param documentId ID of the document
     */
    @Step("Assert retrieved document matches expected document with ID {documentId}")
    public void assertDocumentMatches(String retrievedDocument, String expectedDocument, String documentId) {
        assertNotNull(retrievedDocument, "Retrieved document is null for ID: " + documentId);
        assertEquals(expectedDocument, retrievedDocument, 
                "Retrieved document does not match the indexed document for ID: " + documentId);
    }
}