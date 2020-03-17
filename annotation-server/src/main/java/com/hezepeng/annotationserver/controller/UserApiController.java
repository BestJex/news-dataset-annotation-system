package com.hezepeng.annotationserver.controller;

import com.hezepeng.annotationserver.annotation.NeedAdminAuthorize;
import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

    @PostMapping("/save")
    public ServerResponse save(@RequestBody String s) {
        String path = "/Users/hezepeng/Downloads/result.txt";
        File file = new File(path);

        String content = s + "\n" + "\n";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(content.getBytes());

            fileOutputStream.close();
            return ServerResponse.createBySuccessMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("保存标签结果到文件出错");
        }

    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ServerResponse register(@RequestBody User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ServerResponse update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @NeedAdminAuthorize
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ServerResponse delete(HttpServletRequest request,@RequestBody User user) {
        return userService.deleteUser(request, user.getUsername());
    }

    @NeedAdminAuthorize
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ServerResponse list(HttpServletRequest request) {
        return userService.getAllUserTask(request);
    }

    @RequestMapping(value = "checkPassword", method = RequestMethod.POST)
    public ServerResponse checkPassword(HttpServletRequest request, @RequestBody User user) {
        return userService.checkPassword(request, user.getPassword());
    }

    @RequestMapping(value = "checkUsername", method = RequestMethod.POST)
    public ServerResponse checkUsername(HttpServletRequest request, String username) {
        return userService.checkPassword(request, username);
    }
}
