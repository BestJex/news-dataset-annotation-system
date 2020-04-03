package com.hezepeng.annotationserver.service;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.NewsSimilarity;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.entity.bo.AnnotationTask;
import com.hezepeng.annotationserver.entity.bo.NewsBo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 提交机器学习基础
     *
     * @param skill
     */
    ServerResponse createFoundationSkill(HttpServletRequest request, Integer skill);

    /**
     * 获取并设置机器学习模型
     */
    ServerResponse getOrSetMachineLearningModel(HttpServletRequest request);

    /**
     * 获取并刷新机器学习任务最新的进度状态
     */
    ServerResponse getAndUpdateMachineLearningTaskState(HttpServletRequest request);

    /**
     * 设置模型名称、模型介绍
     *
     * @param user
     */
    ServerResponse createModelInfo(HttpServletRequest request, User user);

    /**
     * 获取用户任务完成情况
     */
    ServerResponse getUserTaskInfo(HttpServletRequest request);

    /**
     * 获取可分配的新闻数
     */
    ServerResponse GetUndoNewsCount();

    /**
     * 新增新闻是否同一件事
     */
    ServerResponse addSimilarity(HttpServletRequest request, NewsSimilarity similarity);

    /**
     * 删除用户标注的数据 保留新闻 但删除该新闻的所有标注信息(包括其他人的)
     */
    ServerResponse deleteUserNews(HttpServletRequest request, User user);

    /**
     * 统计任务标注情况
     */
    ServerResponse taskStatistic();
}
