package com.hezepeng.annotationserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 21:11
 * 需要管理员权限才能操作方法的注释
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedAdminAuthorize {
}
