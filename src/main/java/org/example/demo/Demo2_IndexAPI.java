package org.example.demo;

import com.alibaba.fastjson.JSONReader;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Demo2_IndexAPI {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "book";

    @Test
    public void createIndex() {
        //1. 创建请求对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexToOperate);

        //2. 加载索引配置信息
        JSONReader jsonReader = new JSONReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("index_book.json"))));
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
    public void existsIndex() {
        // 1. 创建request对象
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexToOperate);
        //2. ESClient执行请求
        try {
            boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            System.out.println("exists = " + exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteIndex() {
        //1. 创建请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexToOperate);
        try {
            //2. 客户端执行请求.  Note: 如果索引不存在, 删除会报错.
            AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            //3. 响应输出.
            System.out.println("deleted = " + acknowledgedResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
