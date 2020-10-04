package org.example.sms.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.example.entity.SmsLog;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;

public class TestOtherQuery {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "sms-log-index";

    @Test
    public void prefixQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(QueryBuilders.prefixQuery(SmsLog.CORP_NAME, "阿"));
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void fuzzyQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(QueryBuilders
                        .fuzzyQuery(SmsLog.CORP_NAME, "阿里银行")
                        .fuzziness(Fuzziness.TWO)
                        .prefixLength(0)
                );
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

}
