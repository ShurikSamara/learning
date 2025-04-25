package clients.base.opensearch.impl;

import clients.base.opensearch.interfaces.IOpenSearch;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.action.DocWriteResponse;
import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.delete.DeleteResponse;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static config.PropsConfig.getProps;

@Slf4j
public class OpenSearchImpl implements IOpenSearch {

    private final String OPENSEARCH_USERNAME = getProps().openSearchUsername();
    private final String OPENSEARCH_PASSWORD = getProps().openSearchPassword();

    @Override
    public boolean indexDocument(String url, String index, String id, String document) {
        try (RestHighLevelClient client = createClient(url)) {
            IndexRequest request = new IndexRequest(index)
                    .id(id)
                    .source(document, XContentType.JSON);
            
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response.getResult() == DocWriteResponse.Result.CREATED || 
                   response.getResult() == DocWriteResponse.Result.UPDATED;
        } catch (IOException e) {
            log.error("Error indexing document: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<String> searchDocuments(String url, String index, String query) {
        List<String> results = new ArrayList<>();
        
        try (RestHighLevelClient client = createClient(url)) {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            
            if (query != null && !query.isEmpty()) {
                // If query is a JSON string, we assume it's a raw query
                searchSourceBuilder.query(QueryBuilders.wrapperQuery(query));
            } else {
                // Default to match all if no query provided
                searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            }
            
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                results.add(hit.getSourceAsString());
            }
        } catch (IOException e) {
            log.error("Error searching documents: {}", e.getMessage(), e);
        }
        
        return results;
    }

    @Override
    public String getDocument(String url, String index, String id) {
        try (RestHighLevelClient client = createClient(url)) {
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            
            if (getResponse.isExists()) {
                return getResponse.getSourceAsString();
            }
        } catch (IOException e) {
            log.error("Error getting document: {}", e.getMessage(), e);
        }
        
        return null;
    }

    @Override
    public boolean deleteDocument(String url, String index, String id) {
        try (RestHighLevelClient client = createClient(url)) {
            DeleteRequest request = new DeleteRequest(index, id);
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            
            return response.getResult() == DocWriteResponse.Result.DELETED;
        } catch (IOException e) {
            log.error("Error deleting document: {}", e.getMessage(), e);
            return false;
        }
    }
    
    private RestHighLevelClient createClient(String url) {
        try {
            URI uri = URI.create(url);
            
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(OPENSEARCH_USERNAME, OPENSEARCH_PASSWORD));
            
            return new RestHighLevelClient(
                    RestClient.builder(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()))
                            .setHttpClientConfigCallback(httpClientBuilder -> 
                                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
            );
        } catch (Exception e) {
            log.error("Error creating OpenSearch client: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create OpenSearch client", e);
        }
    }
}