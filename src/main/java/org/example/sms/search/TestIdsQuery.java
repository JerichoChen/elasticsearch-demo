package org.example.sms.search;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.example.entity.SmsLog;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;

public class TestIdsQuery {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "sms-log-index";

    @Test
    public void idQuery() throws IOException {
        GetResponse response = client.get(new GetRequest(indexToOperate, "1"), RequestOptions.DEFAULT);
        if (response.isExists()) {
            System.out.println(response.getSourceAsString());
        } else {
            System.out.println("response.isSourceEmpty() = " + response.isSourceEmpty());
        }
    }

    @Test
    public void idsQuery() throws IOException {
        //1. 创建searchRequest请求对象
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery().addIds("1", "11", "111"));

        searchRequest.source(builder);
        //3. 执行查询请求
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //4. 获取数据, 处理/展示
        System.out.println("response.getHits().getTotalHits() = " + response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

}
