package com.hezepeng.annotationserver.common;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:42
 */
public class Const {
    /**
     * 当前用户的session属性名
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 管理员角色名
     */
    public static final String ADMIN_ROLE = "admin";

    /**
     * token有效期 3600秒
     */
    public static final Long TOKEN_VALID_TIME = 15*3600000L;

    /**
     * token secret
     */
    public static final String TOKEN_SECRET = "NewsGrid";

    /**
     * token 颁发者
     */
    public static final String TOKEN_ISSUER = "Hezepeng";

    /**
     * Headers中储存Token信息的字段名
     */
    public static final String TOKEN_HEADER_NAME = "X-Token";

    public static final String DEFAULT_AVATAR = "https://zeblog-1252705718.cos.ap-guangzhou.myqcloud.com/avatar/sea.jpg";

}
