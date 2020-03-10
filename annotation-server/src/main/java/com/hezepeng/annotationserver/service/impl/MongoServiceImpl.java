package com.hezepeng.annotationserver.service.impl;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.AnnotationRepository;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/9 15:36
 * @description
 */
@Service
public class MongoServiceImpl implements MongoService {
    @Autowired
    AnnotationRepository annotationRepository;

    @Override
    public ServerResponse<Integer> initField() {
        try {
            return ServerResponse.createBySuccess(annotationRepository.initField());
        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }
}
