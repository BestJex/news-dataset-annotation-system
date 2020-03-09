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
    @JsonIgnore
    private ObjectId _id;

    @Transient
    private String id;

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

    /**
     * 新闻分类
     */
    private String news_category;

    // 新闻简介 str类型
    private String news_abstract;

    // 正文内容 str
    private String news_content;

    // 新闻发布时间(当地时间) datetime类型
    private String news_publish_time;

    // 当地国家时区 str类型 如：UTC+8
    private String news_time_zone;

    // 新闻发布时间戳 int类型
    private Integer news_publish_timestamp;

    // 新闻发布的北京时间 datetime类型
    private Date news_publish_beijing_time;

    // 新闻爬取到的时间 datetime类型
    private Date news_crawler_time;

    // 新闻时间发生地点
    private String news_place;

    // 新闻标签 list类型
    private List<Tag> news_tags = new LinkedList<>();

    // 新闻来源名称 str类型 默认为news_website_name
    private String news_source_name;

    // 新闻来源地址 str类型 默认为news_website_url
    private String news_source_url;

    // 新闻网站所属国家 str类型
    private String news_country;

    // 新闻网站名称 str类型
    private String news_website_name;

    // 新闻网站url str类型
    private String news_website_url;

    // 新闻语言类型 list类型
    private List<String> news_language;

    // 新闻翻译-英文

    private String news_content_translate_en;

    private String news_title_translate_en;

    private String news_content_translate_cn;

    private String news_title_translate_cn;

    private Integer news_no;

    /**
     * 标注人 1 的数据
     */
    private Integer user_id_1;

    private List<String> news_emotion_1;

    private String news_position_1;

    private String news_subject_1;

    private String news_type_1;

    private boolean news_about_china_1;

    private String news_emotion_basis_1;

    private Date news_annotation_create_time_1;

    private boolean news_delete_1;

    /**
     * 标注人 2 的数据
     */
    private Integer user_id_2;

    private List<String> news_emotion_2;

    private String news_position_2;

    private String news_subject_2;

    private String news_type_2;

    private boolean news_about_china_2;

    private String news_emotion_basis_2;

    private Date news_annotation_create_time_2;

    private boolean news_delete_2;

    /**
     * 标注状态
     */
    private Integer news_state;

    @PersistenceConstructor
    public News(ObjectId _id, String news_title, List<Author> news_author, String news_detail_url, String news_category, String news_abstract, String news_content, String news_publish_time, String news_time_zone, Integer news_publish_timestamp, Date news_publish_beijing_time, Date news_crawler_time, String news_place, List<Tag> news_tags, String news_source_name, String news_source_url, String news_country, String news_website_name, String news_website_url, List<String> news_language, String news_translate_en, String news_content_translate_en, String news_title_translate_en, String news_content_translate_cn, String news_title_translate_cn, Integer news_no) {
        this._id = _id;
        this.id = _id.toHexString();
        this.news_title = news_title;
        this.news_author = news_author;
        this.news_detail_url = news_detail_url;
        this.news_category = news_category;
        this.news_abstract = news_abstract;
        this.news_content = news_content;
        this.news_publish_time = news_publish_time;
        this.news_time_zone = news_time_zone;
        this.news_publish_timestamp = news_publish_timestamp;
        this.news_publish_beijing_time = news_publish_beijing_time;
        this.news_crawler_time = news_crawler_time;
        this.news_place = news_place;
        this.news_tags = news_tags;
        this.news_source_name = news_source_name;
        this.news_source_url = news_source_url;
        this.news_country = news_country;
        this.news_website_name = news_website_name;
        this.news_website_url = news_website_url;
        this.news_language = news_language;
        this.news_content_translate_en = news_content_translate_en;
        this.news_title_translate_en = news_title_translate_en;
        this.news_content_translate_cn = news_content_translate_cn;
        this.news_title_translate_cn = news_title_translate_cn;
        this.news_no = news_no;
    }
}
