package com.hezepeng.annotationserver.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hezepeng.annotationserver.annotation.NeedAdminAuthorize;
import com.hezepeng.annotationserver.common.Const;
import com.hezepeng.annotationserver.common.ResponseCode;
import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.UserRepository;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 14:51
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放通options请求
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        // 如果不是HandlerMethod就返回错误页
        if (!handler.getClass().equals(HandlerMethod.class)) {
            return true;
        }
        String token = request.getHeader(Const.TOKEN_HEADER_NAME);
        if (token == null) {
            sendErrorMsg(response, ResponseCode.NEED_LOGIN.getCode(), "请先进行登录");
            return false;
        }
        Claims claims = null;
        try {
            claims = TokenUtil.parseToken(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // token过期 需要重新登录
            sendErrorMsg(response, ResponseCode.NEED_LOGIN.getCode(), "登录信息已过期，请重新登录");
            return false;
        } catch (io.jsonwebtoken.SignatureException e) {
            // token解析错误 非法的token
            sendErrorMsg(response, ResponseCode.NEED_LOGIN.getCode(), "身份验证失败，请重新登录");
        } catch (JwtException ex) {
            // 未知的其他错误
            sendErrorMsg(response, ResponseCode.NEED_LOGIN.getCode(), "身份验证异常，请重新登录");
        }
        if (claims == null) {
            return false;
        }
        Integer userId = Integer.valueOf(claims.getId());
        String username = claims.getSubject();
        String issuer = claims.getIssuer();
        Date expirationTime = claims.getExpiration();
        User user = userRepository.selectUserByUsername(username);
        Date now = new Date();

        // 查看该request方法的注解，是否需要管理员权限
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NeedAdminAuthorize need = handlerMethod.getMethodAnnotation(NeedAdminAuthorize.class);
        if (need != null && !user.getRole().contains(Const.ADMIN_ROLE)) {
            sendErrorMsg(response, ResponseCode.NO_PERMISSION.getCode(), "没有足够的权限，无法操作");
            return false;
        }

        // 验证签名
        if (user.getUserId().equals(userId) && issuer.equals(Const.TOKEN_ISSUER) && now.before(expirationTime)) {
            return true;
        } else {
            sendErrorMsg(response, ResponseCode.NEED_LOGIN.getCode(), "身份验证失败，请重新登录");
            return false;
        }
    }

    private void sendErrorMsg(HttpServletResponse response, int errorCode, String msg) {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String responseContent = JSONObject.toJSONString(ServerResponse.createByErrorCodeMessage(errorCode, msg));
        try {
            PrintWriter pw = response.getWriter();
            pw.write(responseContent);
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
