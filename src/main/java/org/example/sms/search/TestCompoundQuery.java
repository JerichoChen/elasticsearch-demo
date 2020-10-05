package org.example.sms.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.example.entity.SmsLog;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class TestCompoundQuery {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "sms-log-index";

    @Test
    public void boolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.boolQuery()
                // 在有filter的情况下, 1<10% 和1<90% 结果不一样. 没有filter,只有should时,结果一样.
                .minimumShouldMatch("1<10%")
                .must(QueryBuilders.rangeQuery(SmsLog.FEE).gt(1).lt(100).queryName("GT"))
                .should(QueryBuilders.matchQuery(SmsLog.CORP_NAME, "杭州").queryName("HZ"))
                .should(QueryBuilders.matchQuery(SmsLog.CORP_NAME, "爸爸").queryName("BA"))
                .filter(QueryBuilders.matchQuery(SmsLog.SMS_CONTENT, "杭州").queryName("XX-Filter"))
        );

        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.printf("%s | %f |%s", Arrays.toString(hit.getMatchedQueries()), hit.getScore(), hit.getSourceAsString());
            System.out.println();
        }
    }


    @Test
    public void boostingQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.boostingQuery(
                QueryBuilders.matchQuery(SmsLog.CORP_NAME, "杭州").queryName("positive"),
                QueryBuilders.rangeQuery(SmsLog.FEE).gt(50).queryName("negative")
        ).negativeBoost(0.3f));

        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.printf("%s | %f |%s", Arrays.toString(hit.getMatchedQueries()), hit.getScore(), hit.getSourceAsString());
            System.out.println();
        }
    }
}
