package org.example.sms.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.entity.SmsLog;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class CreateIndexAndDoc {
    RestHighLevelClient client = ESClient.getInstance();

    String indexToOperate = "sms-log-index";
    String settings = "sms-log-index.json";

    @Test
    public void createIndex() {
        //1. 创建请求对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexToOperate);

        //2. 加载索引配置信息
        JSONReader jsonReader = new JSONReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(settings))));
        String source = jsonReader.readString();
        createIndexRequest.source(source, XContentType.JSON);

        //3. 通过client对象去连接es, 并执行创建请求
        try {
            CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println("isAcknowledged = " + response.isAcknowledged());
            System.out.println("isShardsAcknowledged = " + response.isShardsAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mockData() {
        //1. 创建批量请求
        BulkRequest bulkRequest = new BulkRequest();
        //2. 模拟生成源数据
        List<SmsLog> smsLogList = MockDataUtil.mock();
        //3. 往批量请求中添加doc数据
        smsLogList.forEach(smsLog -> {
            String jsonString = JSON.toJSONString(smsLog);
            IndexRequest indexRequest = new IndexRequest(indexToOperate).id(smsLog.getId() + "").source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        try {
            //4. 执行请求
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("bulkResponse.hasFailures() = " + bulkResponse.hasFailures());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
