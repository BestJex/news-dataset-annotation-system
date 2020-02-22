package com.hezepeng.annotationserver.service.impl;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.AnnotationRepository;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            System.out.println(id);
            News data = annotationRepository.findOneAnnotationNewsById(id);
            return ServerResponse.createBySuccess(data);

        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }
}