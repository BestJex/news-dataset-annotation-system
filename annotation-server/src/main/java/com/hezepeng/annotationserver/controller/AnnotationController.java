package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/getNewsAnnotationList")
    public ServerResponse getNewsAnnotationList(HttpServletRequest request) {
        return annotationService.getNewsAnnotationList(request);
    }

    @GetMapping("/list")
    public ServerResponse GetAnnotationList(HttpServletRequest request) {
        return annotationService.getNewsAnnotationList(request);
    }

    @PostMapping("/add")
    public ServerResponse addNewsAnnotation(HttpServletRequest request, @RequestBody NewsAnnotation annotation) {
        System.out.println(annotation);
        return annotationService.addNewsAnnotation(request, annotation);
    }

    @GetMapping("/get/{id}")
    public ServerResponse getOneNewsAnnotation(@PathVariable String id) {
        return annotationService.getOneNewsAnnotationById(id);
    }

    @GetMapping("/getAnnotationIdList")
    public ServerResponse getAnnotationIdList() {
        return annotationService.getAnnotationIdList();
    }
}
