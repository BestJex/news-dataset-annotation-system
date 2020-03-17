package com.hezepeng.annotationserver.dao;

import com.hezepeng.annotationserver.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/21 00:40
 */
@Component
@Slf4j
public class AnnotationRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<News> findAllAnnotationNewsList() {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en");
        return mongoTemplate.find(query, News.class);
    }

    public News findOneSimpleNewsById(String id) {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content");
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, News.class);
    }

    public News findOneNewsById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, News.class);
    }

    public List<News> findAllAnnotationId() {
        Query query = new Query();
        query.fields().include("_id").include("news_state").include("news_annotation_done");
        query.with(Sort.by(Sort.Order.desc("news_annotation_done")).and(Sort.by(Sort.Order.asc("_id"))));
        return mongoTemplate.find(query, News.class);
    }

    public Integer initField() {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("news_state").is(null));
            List<News> newsList = mongoTemplate.findAll(News.class);
            AtomicInteger updateCount = new AtomicInteger();
            newsList.forEach(news -> {
                boolean needUpdate = initField(news);
                if (needUpdate) {
                    updateCount.getAndIncrement();
                }

            });
            return updateCount.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean initField(News news) {
        if (news.getInit()) {
            return false;
        }
        boolean needUpdate = false;
        // 给校对留一个存储位置
        int userCount = news.getUsers().size();
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
                        try {
                            field.set(news, list);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else if (typeName.equals(Boolean.class.getName())) {
                        LinkedList<Boolean> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        try {
                            field.set(news, list);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else if (typeName.equals(Date.class.getName())) {
                        LinkedList<Date> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        try {
                            field.set(news, list);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else if (typeName.equals(String.class.getName())) {
                        LinkedList<String> list = new LinkedList<>();
                        for (int i = 0; i < userCount; i++) {
                            list.add(null);
                        }
                        try {
                            field.set(news, list);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // 初始化标注状态
            if (type == Integer.class) {
                try {
                    Object value = field.get(news);
                    if (value == null && field.getName().equals("news_state")) {
                        needUpdate = true;
                        // 设置news_state
                        field.set(news, 0);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (needUpdate) {
            mongoTemplate.save(news);
        }
        return needUpdate;

    }
}
