package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 21:25
 */
@RestController
@RequestMapping("api/user/")
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ServerResponse login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("info")
    public ServerResponse info(HttpServletRequest request) {
        return userService.getUserInfo(request);
    }
}
