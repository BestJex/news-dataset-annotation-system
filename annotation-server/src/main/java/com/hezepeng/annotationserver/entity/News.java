package com.hezepeng.annotationserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:45
 */
@Data
@ToString
@Document(collection = "hezepeng")
public class News {
    @Id
    @Field("_id")
    private ObjectId _id;

    /**
     * 新闻标题 str类型
     */
    private String news_title;

    /**
     * 新闻作者 list类型
     */
    private List<Author> news_author = new LinkedList<>();

    /**
     * 新闻详情页url str类型
     */
    private String news_detail_url;

    // 正文内容 str
    private String news_content;

    // 新闻发布时间戳 int类型
    private Integer news_publish_timestamp;

    // 新闻爬取到的时间 datetime类型
    private Date news_crawler_time;

    // 新闻标签 list类型
    private List<Tag> news_tags = new LinkedList<>();

    // 新闻来源地址 str类型 默认为news_website_url
    private String news_source_url;

    // 新闻网站所属国家 str类型
    private String news_country;

    // 新闻翻译-英文
    private String news_content_translate_en;

    private String news_title_translate_en;

    private String news_content_translate_cn;

    private String news_title_translate_cn;

    /**
     * 标注人  的数据
     */
    private LinkedList<String> users;
    private LinkedList<LinkedList<String>> news_emotion;
    private LinkedList<String> news_position;
    private LinkedList<String> news_subject;
    private LinkedList<String> news_type;
    private LinkedList<Boolean> news_about_china;
    private LinkedList<String> news_emotion_basis;
    private LinkedList<Date> news_annotation_create_time;
    private LinkedList<Boolean> news_delete;

    /**
     * 标注状态
     */
    private Integer news_state;

    private Boolean init = false;

}
