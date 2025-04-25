package clients;

import clients.base.opensearch.interfaces.IOpenSearch;
import lombok.extern.slf4j.Slf4j;
import utils.ZipkinTracer;

import java.util.List;

import static config.PropsConfig.getProps;

/**
 * Client for OpenSearch operations
 */
@Slf4j
public class OpenSearchClient {

    private final IOpenSearch openSearch;
    private final String url;

    /**
     * Constructor with OpenSearch implementation
     * 
     * @param openSearch OpenSearch implementation
     */
    public OpenSearchClient(IOpenSearch openSearch) {
        this.openSearch = openSearch;
        this.url = getProps().openSearchUrl();
    }

    /**
     * Create or update a document in OpenSearch
     * 
     * @param index Index name
     * @param id Document ID
     * @param document Document as JSON string
     * @return true if successful
     */
    public boolean indexDocument(String index, String id, String document) {
        log.info("Indexing document with ID {} in index {}", id, index);
        String spanId = ZipkinTracer.startSpan("OpenSearchClient.indexDocument");
        try {
            ZipkinTracer.addTag("index", index);
            ZipkinTracer.addTag("id", id);
            ZipkinTracer.addTag("url", url);

            return openSearch.indexDocument(url, index, id, document);
        } finally {
            ZipkinTracer.endSpan(spanId);
        }
    }

    /**
     * Search for documents in OpenSearch
     * 
     * @param index Index name
     * @param query Query as JSON string
     * @return List of documents as JSON strings
     */
    public List<String> searchDocuments(String index, String query) {
        log.info("Searching documents in index {} with query: {}", index, query);
        String spanId = ZipkinTracer.startSpan("OpenSearchClient.searchDocuments");
        try {
            ZipkinTracer.addTag("index", index);
            ZipkinTracer.addTag("url", url);

            return openSearch.searchDocuments(url, index, query);
        } finally {
            ZipkinTracer.endSpan(spanId);
        }
    }

    /**
     * Get a document from OpenSearch by ID
     * 
     * @param index Index name
     * @param id Document ID
     * @return Document as JSON string
     */
    public String getDocument(String index, String id) {
        log.info("Getting document with ID {} from index {}", id, index);
        String spanId = ZipkinTracer.startSpan("OpenSearchClient.getDocument");
        try {
            ZipkinTracer.addTag("index", index);
            ZipkinTracer.addTag("id", id);
            ZipkinTracer.addTag("url", url);

            return openSearch.getDocument(url, index, id);
        } finally {
            ZipkinTracer.endSpan(spanId);
        }
    }

    /**
     * Delete a document from OpenSearch
     * 
     * @param index Index name
     * @param id Document ID
     * @return true if successful
     */
    public boolean deleteDocument(String index, String id) {
        log.info("Deleting document with ID {} from index {}", id, index);
        String spanId = ZipkinTracer.startSpan("OpenSearchClient.deleteDocument");
        try {
            ZipkinTracer.addTag("index", index);
            ZipkinTracer.addTag("id", id);
            ZipkinTracer.addTag("url", url);

            return openSearch.deleteDocument(url, index, id);
        } finally {
            ZipkinTracer.endSpan(spanId);
        }
    }
}
