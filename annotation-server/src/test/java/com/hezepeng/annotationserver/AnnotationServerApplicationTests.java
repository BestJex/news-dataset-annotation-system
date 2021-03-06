package com.hezepeng.annotationserver;

import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.util.MD5Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@Slf4j
class AnnotationServerApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("caohaoshuang");
        user.setPassword(MD5Util.getMD5Upper("caohaoshuang"));
        user.setName("曹皓爽");
        user.setEmail("275688470@qq.com");
        List<String> role = new ArrayList<>();
//        role.add("admin");
        role.add("editor");
        user.setRole(role);
        user.setMachineLearningTaskState(0);
        mongoTemplate.insert(user);
    }


    @Test
    void findNews() {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId("5e3ffbe33525447237a2dcc7")));
        News news = mongoTemplate.findOne(query, News.class);
        System.out.println(news.getNews_publish_beijing_time());
    }

    @Test
    void updateUser() {
        Query query = new Query().addCriteria(Criteria.where("username").is("hezepeng"));
        User user = mongoTemplate.findOne(query, User.class);
        mongoTemplate.save(user);
    }

    @Test
    void linkedListTest() throws IllegalAccessException {
        Query query = new Query();
        News news = mongoTemplate.findOne(query, News.class);
        int userCount = 3;
        for (Field field : news.getClass().getDeclaredFields()) {
            if (field.getName().equals("users")) {
                continue;
            }
            Type type = field.getType();
            field.setAccessible(true);
            if (type == LinkedList.class) {
                Type genericType = field.getGenericType();
                // 如果是泛型参数的类型
                if (genericType instanceof ParameterizedType) {
                    // 获得泛型参数的类型
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里子类型
                    String typeName = pt.getActualTypeArguments()[0].getTypeName();
                    if (typeName.contains(LinkedList.class.getName())) {
                        LinkedList<LinkedList<String>> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        field.set(news, list);
                    } else if (typeName.equals(Boolean.class.getName())) {
                        LinkedList<Boolean> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        field.set(news, list);
                    } else if (typeName.equals(Date.class.getName())) {
                        LinkedList<Date> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        field.set(news, list);
                    } else if (typeName.equals(String.class.getName())) {
                        LinkedList<String> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        field.set(news, list);
                    }
                }
            }
        }
    }

    @Test
    public void ifTest() {
        List<News> list = new LinkedList<>();
        list.add(new News());
        list.add(new News());
        News news = list.get(1);
        news.setNews_about_china(new LinkedList<>());
        System.out.println(list);

        LinkedList<Boolean> aboutChina = news.getNews_about_china();
        aboutChina.add(0, false);
//            news.setNews_about_china(aboutChina);
        System.out.println(list);
    }

}
