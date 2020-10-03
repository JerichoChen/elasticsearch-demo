package org.example.sms.search;

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

public class TestTermQuery {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "sms-log-index";

    @Test
    public void termQuery() throws IOException {
        //1. 创建searchRequest请求对象
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0);
        builder.size(2);
        builder.query(QueryBuilders.termQuery(SmsLog.PROVINCE, "浙江").boost(1.0f));
        //先按IP排序, 再按FEE排序
        builder.sort(SmsLog.IP_ADDR, SortOrder.DESC);
        builder.sort(SmsLog.FEE, SortOrder.DESC);

        searchRequest.source(builder);
        //3. 执行查询请求
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //4. 获取数据, 处理/展示
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void termsQuery() throws IOException {
        //1. 创建searchRequest请求对象
        SearchRequest searchRequest = new SearchRequest(indexToOperate);
        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0);
        builder.size(2);
        builder.query(QueryBuilders.termsQuery(SmsLog.PROVINCE, "北京","上海").boost(1.0f));
        //先按IP排序, 再按FEE排序
        builder.sort(SmsLog.IP_ADDR, SortOrder.DESC);
        builder.sort(SmsLog.FEE, SortOrder.DESC);

        searchRequest.source(builder);
        //3. 执行查询请求
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //4. 获取数据, 处理/展示
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }


}
