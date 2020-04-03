package com.hezepeng.annotationserver.entity;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/10 00:21
 * @description
 */
@Data
public class NewsAnnotation {
    private String _id;
    /**
     * 标注人  的数据
     */
    private String user;

    private LinkedList<String> news_emotion;

    private String news_position;

    private String news_subject;

    private String news_type;

    private Boolean news_about_china;

    private LinkedList<String> news_emotion_basis;

    private Date news_annotation_create_time;

    private Boolean news_delete;
}
