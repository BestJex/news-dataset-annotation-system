package com.hezepeng.annotationserver.service.impl;

import com.hezepeng.annotationserver.common.Const;
import com.hezepeng.annotationserver.common.ResponseCode;
import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.UserRepository;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.service.UserService;
import com.hezepeng.annotationserver.util.MD5Util;
import com.hezepeng.annotationserver.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 21:29
 */
@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ServerResponse<Map<String, String>> login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage("用户名或密码不能为空！");
        }
        User user = userRepository.selectUserByUsername(username);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        password = MD5Util.getMD5Upper(password);
        user = userRepository.selectUserByUsernameAndPassword(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        // 记录登录时间、增加登录次数
        user.setLastLoginTime(new Date());
        log.info(user.toString());
        userRepository.updateUser(user);
        // 签发token
        String token = TokenUtil.createJWT(user.getUserId().toString(), user.getUsername());
        Map<String, String> resultMap = new HashMap<>(10);
        resultMap.put("token", token);
        return ServerResponse.createBySuccess("登陆成功", resultMap);
    }

    @Override
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        Integer userId = TokenUtil.getUserIdFromRequest(request);
        User user = userRepository.selectUserById(userId);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"身份信息异常，请重新登陆!");
        }
        user.setPassword(null);
        return ServerResponse.createBySuccess(user);
    }
}
