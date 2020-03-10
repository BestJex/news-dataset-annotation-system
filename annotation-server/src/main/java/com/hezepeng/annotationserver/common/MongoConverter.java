package com.hezepeng.annotationserver.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.hezepeng.annotationserver.entity.Tag;
import org.springframework.data.convert.ReadingConverter;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/20 00:53
 * 定义mongo的字典字段、时间字段到Java实体类、时间的转换器
 */

@ReadingConverter
public class MongoConverter implements Converter<String, Tag>{

    @Override
    public Tag convert(String s) {
        return null;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}