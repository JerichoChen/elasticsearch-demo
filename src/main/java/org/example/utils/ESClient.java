package org.example.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESClient {
    public static RestHighLevelClient INSTANCE;

    //创建RestHighLevelClient
    public static RestHighLevelClient getInstance() {
        if (INSTANCE == null) {
            HttpHost httpHost = new HttpHost("localhost", 9200);
            INSTANCE = new RestHighLevelClient(RestClient.builder(httpHost));
        }
        return INSTANCE;
    }

    //关闭客户端连接
    public static void close(RestHighLevelClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
