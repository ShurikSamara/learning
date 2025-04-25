package clients.base.opensearch.interfaces;

import java.util.List;

/**
 * Interface for OpenSearch operations
 */
public interface IOpenSearch {
    
    /**
     * Create or update a document in OpenSearch
     * 
     * @param url OpenSearch URL
     * @param index Index name
     * @param id Document ID
     * @param document Document as JSON string
     * @return true if successful
     */
    boolean indexDocument(String url, String index, String id, String document);
    
    /**
     * Search for documents in OpenSearch
     * 
     * @param url OpenSearch URL
     * @param index Index name
     * @param query Query as JSON string
     * @return List of documents as JSON strings
     */
    List<String> searchDocuments(String url, String index, String query);
    
    /**
     * Get a document from OpenSearch by ID
     * 
     * @param url OpenSearch URL
     * @param index Index name
     * @param id Document ID
     * @return Document as JSON string
     */
    String getDocument(String url, String index, String id);
    
    /**
     * Delete a document from OpenSearch
     * 
     * @param url OpenSearch URL
     * @param index Index name
     * @param id Document ID
     * @return true if successful
     */
    boolean deleteDocument(String url, String index, String id);
}