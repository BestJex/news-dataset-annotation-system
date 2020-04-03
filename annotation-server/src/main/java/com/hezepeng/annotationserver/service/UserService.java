package com.hezepeng.annotationserver.service;

import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 21:26
 */
public interface UserService {
    ServerResponse<Map<String, String>> login(String username, String password);

    ServerResponse<User> getUserInfo(HttpServletRequest request);

    ServerResponse checkPassword(HttpServletRequest request, String password);

    ServerResponse deleteUser(HttpServletRequest request, String username);

    ServerResponse updateUser(User user);

    ServerResponse register(User user);

    ServerResponse getAllUserTask(HttpServletRequest request);

    ServerResponse changePassword(HttpServletRequest request, User user);
}
