package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/9 15:07
 * @description 用管理后台直接对MongoDB操作
 */

@RestController
@RequestMapping("/api/mongo")
public class MongoController {

    @Autowired
    MongoService mongoService;

    @GetMapping("/init")
    public ServerResponse initField() {
        return mongoService.initField();
    }

}
