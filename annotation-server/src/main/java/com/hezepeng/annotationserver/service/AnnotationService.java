package com.hezepeng.annotationserver.service;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.bo.NewsBo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:36
 */
public interface AnnotationService {

    ServerResponse<List<News>> getNewsAnnotationList(HttpServletRequest request);

    ServerResponse<List<News>> getAnnotationListByUsername(HttpServletRequest request);

    ServerResponse<News> getOneNewsAnnotationById(String id);

    ServerResponse<List<NewsBo>> getAnnotationIdList();

    ServerResponse addNewsAnnotation(HttpServletRequest request, NewsAnnotation annotation);
}
