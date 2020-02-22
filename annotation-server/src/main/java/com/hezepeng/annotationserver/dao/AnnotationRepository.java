package com.hezepeng.annotationserver.dao;

import com.hezepeng.annotationserver.entity.News;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/21 00:40
 */
@Component
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

    public News findOneAnnotationNewsById(String id) {
        Query query = new Query();
        query.fields().exclude("news_html_content")
                .exclude("news_content")
                .exclude("news_content_translate_en");
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, News.class);
    }
}
