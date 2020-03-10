package com.hezepeng.annotationserver.service.impl;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.AnnotationRepository;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.entity.bo.NewsBo;
import com.hezepeng.annotationserver.service.AnnotationService;
import com.hezepeng.annotationserver.util.TokenUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 16:56
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Override
    public ServerResponse<List<News>> getNewsAnnotationList(HttpServletRequest request) {
        try {
            List<News> data = annotationRepository.findAllAnnotationNewsList();
            return ServerResponse.createBySuccess(data);

        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse<News> getOneNewsAnnotationById(String id) {
        try {
            News data = annotationRepository.findOneSimpleNewsById(id);
            return ServerResponse.createBySuccess(data);

        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse<List<NewsBo>> getAnnotationIdList() {
        try {
            List<NewsBo> ids = new LinkedList<>();
            List<News> objectIds;
            objectIds = annotationRepository.findAllAnnotationId();
            objectIds.forEach(item -> {
                System.out.println(item.getNews_state());
                ids.add(new NewsBo(item.get_id().toHexString(), item.getNews_state()));
            });
            return ServerResponse.createBySuccess(ids);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse addNewsAnnotation(HttpServletRequest request, NewsAnnotation annotation) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            String id = annotation.get_id();
            System.out.println(username + " " + id);
            News news = annotationRepository.findOneNewsById(id);
            int index = news.getUsers().indexOf(username);
            news.setNews_state(1);
            // 情感
            LinkedList<LinkedList<String>> emotion = news.getNews_emotion();
            emotion.set(index, annotation.getNews_emotion());
            news.setNews_emotion(emotion);
            //立场
            LinkedList<String> position = news.getNews_position();
            position.set(index, annotation.getNews_position());
            news.setNews_position(position);
            //主题
            LinkedList<String> subject = news.getNews_subject();
            subject.set(index, annotation.getNews_subject());
            news.setNews_subject(subject);
            // 类型
            LinkedList<String> type = news.getNews_type();
            type.set(index, annotation.getNews_type());
            news.setNews_type(type);
            // 是否中国相关
            LinkedList<Boolean> aboutChina = news.getNews_about_china();
            aboutChina.set(index, annotation.getNews_about_china());
            news.setNews_about_china(aboutChina);
            // 情感依据
            LinkedList<String> basis = news.getNews_emotion_basis();
            basis.set(index, annotation.getNews_emotion_basis());
            news.setNews_emotion_basis(basis);
            mongoTemplate.save(news);
            return ServerResponse.createBySuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }
}
