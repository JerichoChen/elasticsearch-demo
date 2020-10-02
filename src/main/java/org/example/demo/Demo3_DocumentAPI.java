package org.example.demo;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.example.entity.Book;
import org.example.utils.ESClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo3_DocumentAPI {
    RestHighLevelClient client = ESClient.getInstance();
    String indexToOperate = "book";

    @Test
    public void indexDoc() {
        //0. 创建一个文件对象
        Book book = new Book();
        book.setTitle("小王子");
        book.setAuthor("unknown");
        book.setPublishDate(new Date());
        book.setDesc("很好看55555");
        String jsonString = JSON.toJSONString(book);

        //1. 创建indexRequest
        IndexRequest indexRequest = new IndexRequest(indexToOperate);
        //2. 添加document数据
        //2.1 如果不指定id, 会自动创建id, 可以在response中获取生成的id.
        indexRequest.id("b01");
        //2.2 添加文档数据
        indexRequest.source(jsonString, XContentType.JSON);
        //2.3 本次索引的刷新策略. (测试时,可以使用IMMEDIATE)
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //2.4. 版本控制 只有当设置的版本大于文档当前版本,才能成功.
        indexRequest.versionType(VersionType.EXTERNAL);
        indexRequest.version(6);


        try {
            //3. 执行请求, 处理response
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            String id = response.getId();
            System.out.println("response.getVersion() = " + response.getVersion());
            System.out.println("created! id = " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDoc() {
        //1. 创建GetRequest
        GetRequest getRequest = new GetRequest(indexToOperate, "b01");
        try {
            //2. client执行请求
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            //3. response 处理
            System.out.println("getResponse.isExists() = " + getResponse.isExists());
            if (getResponse.isExists()) {
                System.out.println("getResponse.getVersion() = " + getResponse.getVersion());
                System.out.println("getResponse.getSourceAsString() = " + getResponse.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void existsDoc() {
        //1. 检查某个doc是否存在, 用的请求与GetAPI 一样: GetRequest
        GetRequest getRequest = new GetRequest(indexToOperate, "b01");
        //由于exists返回的仅为true/false, 所以建议关闭获取_source和任何stored fields, 使请求能更轻量一些.
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
        getRequest.storedFields("_none_");

        try {
            //2. client执行请求
            boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
            //3. 结果处理
            System.out.println("exists = " + exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateDoc() {
        //1. 准备要更新的内容, 使用map的方式. 也可以使用JSOString的方式.
        String docId = "b01";
        Map<String, Object> updates = new HashMap<>();
        updates.put("desc", "非常非常好看!!!");

        try {
            //2. 创建update请求
            UpdateRequest updateRequest = new UpdateRequest(indexToOperate, docId);
            updateRequest.doc(updates);

            //3. 执行请求, 处理响应.
            UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println("result = " + response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteDoc() {
        //1. 创建delete请求
        String docId = "b01";
        DeleteRequest deleteRequest = new DeleteRequest(indexToOperate, docId);
        try {
            //2. 执行请求, 处理响应.
            DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println("delete.getResult() = " + delete.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bulkIndexDoc() {
        Book b1 = new Book("1", "AAA", "BBB", new Date(), "CCC");
        Book b2 = new Book("2", "AAA", "BBB", new Date(), "CCC");
        Book b3 = new Book("3", "AAA", "BBB", new Date(), "CCC");

        String j1 = JSON.toJSONString(b1);
        String j2 = JSON.toJSONString(b2);
        String j3 = JSON.toJSONString(b3);

        BulkRequest bulkRequest = new BulkRequest();
        IndexRequest r1 = new IndexRequest(indexToOperate).id(b1.getId()).source(j1, XContentType.JSON);
        IndexRequest r2 = new IndexRequest(indexToOperate).id(b2.getId()).source(j2, XContentType.JSON);
        IndexRequest r3 = new IndexRequest(indexToOperate).id(b3.getId()).source(j3, XContentType.JSON);

        bulkRequest.add(r1);
        bulkRequest.add(r2);
        bulkRequest.add(r3);
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("bulkResponse.hasFailures() = " + bulkResponse.hasFailures());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bulkDeleteDoc() {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest(indexToOperate, "1"));
        bulkRequest.add(new DeleteRequest(indexToOperate, "2"));
        bulkRequest.add(new DeleteRequest(indexToOperate, "3"));
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                bulkItemResponse.getOpType();
            }
            System.out.println("bulkResponse.hasFailures() = " + bulkResponse.hasFailures());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
