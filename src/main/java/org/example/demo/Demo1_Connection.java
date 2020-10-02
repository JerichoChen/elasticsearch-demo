package org.example.demo;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;

public class Demo1_Connection {

    @Test
    public void testConnection() {
        RestHighLevelClient client = ESClient.getInstance();
        try {
            System.out.println(client.cluster()
                    .health(new ClusterHealthRequest("*"), RequestOptions.DEFAULT)
                    .getClusterName()
            );
            System.out.println("ES client connected!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
