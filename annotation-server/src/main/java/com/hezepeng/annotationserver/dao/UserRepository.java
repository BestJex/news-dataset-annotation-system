package com.hezepeng.annotationserver.dao;

import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.entity.bo.UserBo;
import com.hezepeng.annotationserver.util.MD5Util;
import org.bson.io.BsonOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public boolean updatePassword(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(user.getUsername()));
        Update update = new Update();
        update.set("password", MD5Util.getMD5Upper(user.getPassword()));
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0;
    }

    public List<User> selectAllUser() {
        return mongoTemplate.findAll(User.class);
    }

    public int deleteByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return (int) mongoTemplate.remove(query, User.class).getDeletedCount();
    }

    public int addUser(User user) {
        user = mongoTemplate.save(user);
        return 1;
    }

    public List<UserBo> selectAllUserTask() {
        List<User> userList = mongoTemplate.findAll(User.class);
        List<UserBo> data = new ArrayList<>();
        for (int j = 0; j < userList.size(); j++) {
            Query query = new Query();
            query.addCriteria(Criteria.where("users").all(userList.get(j).getUsername()));
            query.fields().include("_id").include("users").include("news_annotation_done").include("news_position");
            List<News> newsLists = mongoTemplate.find(query, News.class);
            int completeTaskCount = 0, revisionTaskCount = 0, undoTaskCount = 0;
            for (int i = 0; i < newsLists.size(); i++) {
                int index = newsLists.get(i).getUsers().indexOf(userList.get(j).getUsername());
                if (newsLists.get(i).getNews_annotation_done().get(index) != null && newsLists.get(i).getNews_annotation_done().get(index)) {
                    completeTaskCount++;
                } else if (newsLists.get(i).getNews_annotation_done().get(index) != null && !newsLists.get(i).getNews_annotation_done().get(index)) {
                    revisionTaskCount++;
                } else {
                    undoTaskCount++;
                }
            }
            UserBo userBo = new UserBo(userList.get(j), completeTaskCount, revisionTaskCount, undoTaskCount);
            data.add(userBo);
        }
        return data;
    }
}
