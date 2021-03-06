package com.hezepeng.annotationserver.dao;

import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsSimilarity;
import com.hezepeng.annotationserver.entity.bo.DeleteUserNewsResult;
import com.hezepeng.annotationserver.entity.bo.TaskStatistic;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
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

    public boolean addSimilarity(NewsSimilarity similarity) {
        try {
            mongoTemplate.save(similarity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void updateNews(News news) {
        mongoTemplate.save(news);
    }

    public int getUndoNewsCount() {
        Query query = new Query();
        query.addCriteria(Criteria.where("news_state").is(null));
        return (int) mongoTemplate.count(query, News.class);
    }

    public int getUserUndoTaskCount(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("users").all(username));
        query.fields().include("users").include("news_annotation_done");
        List<News> newsLists = mongoTemplate.find(query, News.class);
        AtomicInteger count = new AtomicInteger();
        newsLists.forEach(news -> {

            int index = news.getUsers().indexOf(username);
            if (news.getNews_annotation_done().get(index) == null) {
                count.addAndGet(1);
            }
        });
        return count.get();
    }

    public void updateNewsStateById(ObjectId id, Integer state) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("news_state", state);
        mongoTemplate.updateFirst(query, update, News.class);
    }

    public List<News> findAllAnnotationNewsList() {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en")
                .exclude("news_content_translate_cn");
        return mongoTemplate.find(query, News.class);
    }

    public List<News> findAllTaskList() {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en")
                .exclude("news_content_translate_cn");
        query.addCriteria(Criteria.where("init").is(true));
        query.with((Sort.by(Sort.Order.asc("_id"))));
        return mongoTemplate.find(query, News.class);
    }

    public List<News> findTaskListByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("users").all(username));
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en")
                .exclude("news_content_translate_cn");
        query.with((Sort.by(Sort.Order.asc("_id"))));
        return mongoTemplate.find(query, News.class);
    }

    public List<News> findAllAnnotationNewsListBySkipLimit(int skip, int limit) {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en")
                .exclude("news_content_translate_cn");
        query.skip(skip).limit(limit);
        return mongoTemplate.find(query, News.class);
    }

    public List<News> findNewsListByState(Integer state, Integer skip, Integer limit) {
        Query query = new Query();
        if (state != null) {
            query.fields().exclude("news_content")
                    .exclude("news_content_translate_en")
                    .exclude("news_content_translate_cn");
        }
        query.addCriteria(Criteria.where("news_state").is(state));
        if (skip != null) {
            query.skip(skip);
        }
        if (limit != null) {
            query.limit(limit);
        }
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

    public List<News> findAllAnnotationIdByUsername(String username) {
        Query query = new Query();
        query.fields().include("_id").include("users").include("news_state").include("news_annotation_done");
        query.addCriteria(Criteria.where("users").all(username).and("news_state").lt(3));
        query.with(Sort.by(Sort.Order.desc("news_state")).and(Sort.by(Sort.Order.asc("_id"))));
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
            news.setInit(true);
//            news.setNews_state(0);
            mongoTemplate.save(news);
        }
        return needUpdate;

    }

    public DeleteUserNewsResult deleteUserNews(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("users").all(username));
        List<News> list = mongoTemplate.find(query, News.class);
        DeleteUserNewsResult result=new DeleteUserNewsResult();
        AtomicInteger similarityAnnotationCount = new AtomicInteger(0);
        Integer newsCount = list.size();
        list.forEach(news -> {
            news.setNews_state(null);
            news.setNews_annotation_done(null);
            news.setInit(false);
            news.setUsers(null);
            news.setNews_annotation_create_time(null);
            news.setNews_type(null);
            news.setNews_about_china(null);
            news.setNews_emotion(null);
            news.setNews_emotion_basis(null);
            news.setNews_subject(null);
            news.setNews_position(null);
            news.setNews_delete(null);
            Query q = new Query();
            q.addCriteria(Criteria.where("username").is(username));
            mongoTemplate.save(news);
            // 删除该用户的新闻关联标注
            similarityAnnotationCount.getAndAdd((int) mongoTemplate.remove(q, NewsSimilarity.class).getDeletedCount());
        });
        result.setDeleteSimilarityAnnotationCount(similarityAnnotationCount.get());
        result.setInitNewsCount(newsCount);
        return result;
    }

    public TaskStatistic getTaskStatistic() {
        Query query = new Query();
        // 获取新闻总数
        TaskStatistic statistic = new TaskStatistic();
        statistic.setNewsTotalCount(Math.toIntExact(mongoTemplate.count(query, News.class)));

        // 获取累计完成标注
        query = new Query();
        query.addCriteria(Criteria.where("news_state").is(10));
        statistic.setAnnotationDoneCount(Math.toIntExact(mongoTemplate.count(query, News.class)));

        //获取待校验数量
        query = new Query();
        query.addCriteria(Criteria.where("news_state").is(2));
        statistic.setWaitAnnotationCount(Math.toIntExact(mongoTemplate.count(query, News.class)));

        // 获取待仲裁数量
        query = new Query();
        query.addCriteria(Criteria.where("news_state").is(4));
        statistic.setWaitArbitrateCount(Math.toIntExact(mongoTemplate.count(query, News.class)));

        //获取进行中的新闻数量
        query = new Query();
        query.addCriteria(Criteria.where("news_state").is(1));
        statistic.setWaitAnnotationCount(Math.toIntExact(mongoTemplate.count(query, News.class)));

        return statistic;
    }
}
