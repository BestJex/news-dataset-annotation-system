package com.hezepeng.annotationserver.dao;

import com.hezepeng.annotationserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 20:35
 */
@Component
public class UserRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public User selectUserById(Integer userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    public User selectUserByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, User.class);
    }

    public User selectUserByUsernameAndPassword(String username, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("password").is(password));
        return mongoTemplate.findOne(query, User.class);
    }

    public User selectUserByOpenId(String openId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("openId").is(openId));
        return mongoTemplate.findOne(query, User.class);
    }

    public User updateUser(User user) {
        return mongoTemplate.save(user);
    }

    public List<User> selectAllUser() {
        return mongoTemplate.findAll(User.class);
    }

}
