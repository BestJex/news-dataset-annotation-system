package com.hezepeng.annotationserver.service;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.bo.AnnotationTask;
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

    /***
     * 获取指定用户的标注任务列表、如果是管理员身份就返回所有用户的标注任务列表
     */
    ServerResponse<List<News>> getTaskListByUsername(HttpServletRequest request);

    /***
     * 获取指定Id的新闻
     */
    ServerResponse<News> getOneNewsAnnotationById(String id);

    /***
     * 获取指定用户的标注任务的id列表
     */
    ServerResponse<List<NewsBo>> getAnnotationIdList(HttpServletRequest request);

    /***
     * 添加一个新闻标注
     */
    ServerResponse addNewsAnnotation(HttpServletRequest request, NewsAnnotation annotation);

    /***
     * 新建任务分配
     */
    ServerResponse createTask(AnnotationTask userTaskList);

    /**
     * 一键校验
     */
    ServerResponse checkAnnotation();
}
