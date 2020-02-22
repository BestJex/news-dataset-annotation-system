package com.hezepeng.annotationserver.listener;

import com.hezepeng.annotationserver.annotation.AutoIncKey;
import com.hezepeng.annotationserver.entity.IncrementInfo;
import com.hezepeng.annotationserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 15:14
 * 监听MongoDB保存数据事件
 */
@Slf4j
@Component
public class MongoSaveEventListener extends AbstractMongoEventListener<Object> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        User user = (User) event.getSource();
        // 如果没有userId 相当于是新插入对象 需要一个自增id 有userId说明是更新 不是插入
        // 加上这个判断是为了在调用mongoTemplate.save()更新user对象时，不会被赋值新的
        if (user.getUserId() == null) {
            ReflectionUtils.doWithFields(user.getClass(), new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    if (field.isAnnotationPresent(AutoIncKey.class)) {
                        String collName = mongoTemplate.getCollectionName(user.getClass());
                        Integer id = getNextId(collName);
                        field.set(user, id);
                    }
                }
            });
        }

    }

    /**
     * 获取指定collName的最新的自增主键
     * @param collName MongoDB的集合名
     * @return
     */
    private Integer getNextId(String collName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("collName").is(collName));
        Update update = new Update();
        // 加1后返回
        update.inc("incId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, IncrementInfo.class).getIncId();
    }
}
