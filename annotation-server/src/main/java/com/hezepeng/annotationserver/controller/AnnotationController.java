package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:28
 */

@RestController
public class AnnotationController {
    @Autowired
    private AnnotationService annotationService;

    @GetMapping("/list")
    public ServerResponse getAnnotationList(HttpServletRequest request) {
        return annotationService.getNewsAnnotationList(request);
    }
}
