package com.hezepeng.annotationserver.service;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.News;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:36
 */
public interface AnnotationService {
    ServerResponse<List<News>> getNewsAnnotationList(HttpServletRequest request);

    ServerResponse<News> getOneNewsAnnotationById(String id);
}
