package com.lxt.test;

import com.lxt.test.entity.MyChatMessage;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@SpringBootTest
public class MongoDbTest {

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testDelete() {
        Query query = new Query(Criteria.where("id").is("1"));
        DeleteResult result = mongoTemplate.remove(query, MyChatMessage.class);
        System.out.println(result.getDeletedCount());
    }

    @Test
    public void testInsert() {
        MyChatMessage result = mongoTemplate.save(new MyChatMessage("2", "test"));
        System.out.println(result.toString());
    }

    @Test
    public void testUpdate() {
        Query query = new Query(Criteria.where("id").is("1"));
        Update update = new Update();
        update.set("content", "hello world 啸天 ");
        UpdateResult upsert = mongoTemplate.upsert(query, update, MyChatMessage.class);
        System.out.println(upsert.getMatchedCount());
        System.out.println(upsert.getModifiedCount());
    }

    @Test
    public void  testQuery(){
//        Criteria criteria = Criteria.where("id").is(1L);
//        criteria.and("content").is("hello world 啸天 ");

        Criteria criteria = Criteria.where("id").in("1","2");
        Query query = new Query(criteria);
        List<MyChatMessage> list = mongoTemplate.find(query, MyChatMessage.class);
        for (MyChatMessage item : list) {
            System.out.println(item.toString());
        }
    }
}
