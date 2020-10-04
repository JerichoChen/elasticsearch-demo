package org.example.sms.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.example.entity.SmsLog;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;

public class TestMatchQuery {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "sms-log-index";

    /**
     * GET /sms-log-index/_search
     * {
     * "query": {
     * "match": {
     * "corpName": {
     * "query": "巴巴杭州阿里",
     * "analyzer": "ik_max_word",
     * "boost": 0.8,
     * "operator": "or",
     * "minimum_should_match":1,
     * "zero_terms_query": "none"
     * }
     * }
     * }
     * }
     *
     * @throws IOException
     */
    @Test
    public void matchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(SmsLog.CORP_NAME, "巴巴杭州阿里")
                .operator(Operator.OR)
                .analyzer("ik_smart")
                .minimumShouldMatch("2");
        sourceBuilder.query(matchQuery);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void matchAllQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.sort(SmsLog.FEE, SortOrder.ASC);
        builder.sort(SmsLog.ID, SortOrder.ASC);
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void multiMatchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery("阿里", SmsLog.CORP_NAME, SmsLog.SMS_CONTENT));
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }
}
