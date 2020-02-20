package com.hezepeng.annotationserver.entity;

import java.util.List;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 17:19
 */
public class Comment {
    private String name;
    private String content;
    private String publish_time;
    private List<Comment> comment_reply;
    private String time_zone;
    private Integer publish_timestamp;
    private String publish_beijing_time;
}
