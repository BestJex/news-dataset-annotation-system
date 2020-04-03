package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.annotation.NeedAdminAuthorize;
import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.NewsSimilarity;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.entity.bo.AnnotationTask;
import com.hezepeng.annotationserver.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.ServerSocket;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:28
 */

@RestController
@RequestMapping("/api/annotation")
public class AnnotationController {
    @Autowired
    private AnnotationService annotationService;

    @GetMapping("/list")
    public ServerResponse GetAnnotationList(HttpServletRequest request) {
        return annotationService.getNewsAnnotationList(request);
    }

    @GetMapping("/count")
    @NeedAdminAuthorize
    public ServerResponse GetUndoNewsCount() {
        return annotationService.GetUndoNewsCount();
    }

    @PostMapping("/delete/user/news")
    @NeedAdminAuthorize
    public ServerResponse deleteUserNews(HttpServletRequest request, @RequestBody User user) {
        return annotationService.deleteUserNews(request, user);
    }

    @PostMapping("/add")
    public ServerResponse addNewsAnnotation(HttpServletRequest request, @RequestBody NewsAnnotation annotation) {
        return annotationService.addNewsAnnotation(request, annotation);
    }

    @GetMapping("/get/{id}")
    public ServerResponse getOneNewsAnnotation(@PathVariable String id) {
        return annotationService.getOneNewsAnnotationById(id);
    }

    @GetMapping("/task/list")
    public ServerResponse getTaskList(HttpServletRequest request) {
        return annotationService.getTaskListByUsername(request);
    }

    @GetMapping("/id/list")
    public ServerResponse getAnnotationIdList(HttpServletRequest request) {
        return annotationService.getAnnotationIdList(request);
    }

    @PostMapping("/task/create")
    @NeedAdminAuthorize
    public ServerResponse createTask(@RequestBody AnnotationTask task) {
        return annotationService.createTask(task);
    }

    @PostMapping("/check")
    @NeedAdminAuthorize
    public ServerResponse checkAnnotation() {
        return annotationService.checkAnnotation();
    }


    @GetMapping("/task/machine/skill/create/{skill}")
    public ServerResponse createFoundationSkill(HttpServletRequest request, @PathVariable Integer skill) {
        return annotationService.createFoundationSkill(request, skill);
    }

    @PostMapping("/task/machine/model/init")
    public ServerResponse getOrSetMachineLearningModel(HttpServletRequest request) {
        return annotationService.getOrSetMachineLearningModel(request);
    }

    @PostMapping("/task/machine/state/get")
    public ServerResponse getAndUpdateMachineLearningTaskState(HttpServletRequest request) {
        return annotationService.getAndUpdateMachineLearningTaskState(request);
    }

    @PostMapping("/task/machine/model/info")
    public ServerResponse createModelInfo(HttpServletRequest request, @RequestBody User user) {
        return annotationService.createModelInfo(request, user);
    }

    @GetMapping("/task/info")
    public ServerResponse getUserTaskInfo(HttpServletRequest request) {
        return annotationService.getUserTaskInfo(request);
    }

    @PostMapping("/similarity/add")
    public ServerResponse addSimilarity(HttpServletRequest request, @RequestBody NewsSimilarity similarity) {
        return annotationService.addSimilarity(request, similarity);
    }

    @GetMapping("/task/statistic")
    @NeedAdminAuthorize
    public ServerResponse taskStatistic(){
        return annotationService.taskStatistic();
    }

}
