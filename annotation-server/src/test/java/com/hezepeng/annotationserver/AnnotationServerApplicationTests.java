package com.hezepeng.annotationserver;

import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.util.MD5Util;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
@Slf4j
class AnnotationServerApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("hezepeng");
        user.setPassword(MD5Util.getMD5Upper("hezepeng"));
        user.setName("何泽鹏");
        user.setEmail("275688470@qq.com");
        user.setRole("admin");
        mongoTemplate.insert(user);
        log.info(user.getUserId().toString());

    }

    @Test
    void findUser() {
        Query query = new Query().addCriteria(Criteria.where("_id").is(10));
        User user = mongoTemplate.findOne(query, User.class);
        log.info(user.toString());
    }

}
