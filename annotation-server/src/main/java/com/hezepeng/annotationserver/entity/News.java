package com.hezepeng.annotationserver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 14:45
 */
@Document(collection = "hezepeng")
public class News {
    @Id
    @Field("_id")
    private String _id;

    /**
     * 新闻标题 str类型
     */
    private String news_title;

    /**
     * 新闻作者 list类型
     */
    private List<Author> news_author=new LinkedList<>();

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

    // 新闻阅读数 int类型
    private Integer news_read_count;

    // 新闻标签 list类型
    private List<Tag> news_tags=new LinkedList<>();

    // 是否转载 boolean类型
    private boolean news_is_reprint;

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

    // 新闻详情页编码方式 str类型
    private String news_encoding;

    // 新闻评论 list类型
    private List<Comment> news_comments=new LinkedList<>();

    // 新闻点赞数
    private Integer news_like_count;

    // 是否需要proxy
    private boolean news_proxy;

    // 新闻图片oss地址
    private String news_oss_image_url;

    // 新闻图片源站地址
    private String news_image_url;

    // 新闻图片img标签中的alt属性值
    private String news_image_alt;

    // 新闻图片img标签中的description、title之类的存到这个字段
    private String news_image_description;

    // 新闻内容html
    private String news_html_content;

    // 新闻翻译-英文
    private String news_translate_en;

    private String news_content_translate_en;

    private String news_title_translate_en;

    private String news_content_translate_cn;

    private String news_title_translate_cn;

    private Integer news_no;

    @PersistenceConstructor
    public News(String _id, String news_title, List<Author> news_author, String news_detail_url, String news_category, String news_abstract, String news_content, String news_publish_time, String news_time_zone, Integer news_publish_timestamp, Date news_publish_beijing_time, Date news_crawler_time, String news_place, Integer news_read_count, List<Tag> news_tags, boolean news_is_reprint, String news_source_name, String news_source_url, String news_country, String news_website_name, String news_website_url, List<String> news_language, String news_encoding, List<Comment> news_comments, Integer news_like_count, boolean news_proxy, String news_oss_image_url, String news_image_url, String news_image_alt, String news_image_description, String news_html_content, String news_translate_en, String news_content_translate_en, String news_title_translate_en, String news_content_translate_cn, String news_title_translate_cn, Integer news_no) {
        System.out.println(news_tags);
        this._id = _id;
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
        this.news_read_count = news_read_count;
        this.news_tags = news_tags;
        this.news_is_reprint = news_is_reprint;
        this.news_source_name = news_source_name;
        this.news_source_url = news_source_url;
        this.news_country = news_country;
        this.news_website_name = news_website_name;
        this.news_website_url = news_website_url;
        this.news_language = news_language;
        this.news_encoding = news_encoding;
        this.news_comments = news_comments;
        this.news_like_count = news_like_count;
        this.news_proxy = news_proxy;
        this.news_oss_image_url = news_oss_image_url;
        this.news_image_url = news_image_url;
        this.news_image_alt = news_image_alt;
        this.news_image_description = news_image_description;
        this.news_html_content = news_html_content;
        this.news_translate_en = news_translate_en;
        this.news_content_translate_en = news_content_translate_en;
        this.news_title_translate_en = news_title_translate_en;
        this.news_content_translate_cn = news_content_translate_cn;
        this.news_title_translate_cn = news_title_translate_cn;
        this.news_no = news_no;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public List<Author> getNews_author() {
        return news_author;
    }

    public void setNews_author(List<Author> news_author) {
        this.news_author = news_author;
    }

    public String getNews_detail_url() {
        return news_detail_url;
    }

    public void setNews_detail_url(String news_detail_url) {
        this.news_detail_url = news_detail_url;
    }

    public String getNews_category() {
        return news_category;
    }

    public void setNews_category(String news_category) {
        this.news_category = news_category;
    }

    public String getNews_abstract() {
        return news_abstract;
    }

    public void setNews_abstract(String news_abstract) {
        this.news_abstract = news_abstract;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getNews_publish_time() {
        return news_publish_time;
    }

    public void setNews_publish_time(String news_publish_time) {
        this.news_publish_time = news_publish_time;
    }

    public String getNews_time_zone() {
        return news_time_zone;
    }

    public void setNews_time_zone(String news_time_zone) {
        this.news_time_zone = news_time_zone;
    }

    public Integer getNews_publish_timestamp() {
        return news_publish_timestamp;
    }

    public void setNews_publish_timestamp(Integer news_publish_timestamp) {
        this.news_publish_timestamp = news_publish_timestamp;
    }

    public Date getNews_publish_beijing_time() {
        return news_publish_beijing_time;
    }

    public void setNews_publish_beijing_time(Date news_publish_beijing_time) {
        this.news_publish_beijing_time = news_publish_beijing_time;
    }

    public Date getNews_crawler_time() {
        return news_crawler_time;
    }

    public void setNews_crawler_time(Date news_crawler_time) {
        this.news_crawler_time = news_crawler_time;
    }

    public String getNews_place() {
        return news_place;
    }

    public void setNews_place(String news_place) {
        this.news_place = news_place;
    }

    public Integer getNews_read_count() {
        return news_read_count;
    }

    public void setNews_read_count(Integer news_read_count) {
        this.news_read_count = news_read_count;
    }

    public List<Tag> getNews_tags() {
        return news_tags;
    }

    public void setNews_tags(List<Tag> news_tags) {
        this.news_tags = news_tags;
    }

    public boolean isNews_is_reprint() {
        return news_is_reprint;
    }

    public void setNews_is_reprint(boolean news_is_reprint) {
        this.news_is_reprint = news_is_reprint;
    }

    public String getNews_source_name() {
        return news_source_name;
    }

    public void setNews_source_name(String news_source_name) {
        this.news_source_name = news_source_name;
    }

    public String getNews_source_url() {
        return news_source_url;
    }

    public void setNews_source_url(String news_source_url) {
        this.news_source_url = news_source_url;
    }

    public String getNews_country() {
        return news_country;
    }

    public void setNews_country(String news_country) {
        this.news_country = news_country;
    }

    public String getNews_website_name() {
        return news_website_name;
    }

    public void setNews_website_name(String news_website_name) {
        this.news_website_name = news_website_name;
    }

    public String getNews_website_url() {
        return news_website_url;
    }

    public void setNews_website_url(String news_website_url) {
        this.news_website_url = news_website_url;
    }

    public List<String> getNews_language() {
        return news_language;
    }

    public void setNews_language(List<String> news_language) {
        this.news_language = news_language;
    }

    public String getNews_encoding() {
        return news_encoding;
    }

    public void setNews_encoding(String news_encoding) {
        this.news_encoding = news_encoding;
    }

    public List<Comment> getNews_comments() {
        return news_comments;
    }

    public void setNews_comments(List<Comment> news_comments) {
        this.news_comments = news_comments;
    }

    public Integer getNews_like_count() {
        return news_like_count;
    }

    public void setNews_like_count(Integer news_like_count) {
        this.news_like_count = news_like_count;
    }

    public boolean isNews_proxy() {
        return news_proxy;
    }

    public void setNews_proxy(boolean news_proxy) {
        this.news_proxy = news_proxy;
    }

    public String getNews_oss_image_url() {
        return news_oss_image_url;
    }

    public void setNews_oss_image_url(String news_oss_image_url) {
        this.news_oss_image_url = news_oss_image_url;
    }

    public String getNews_image_url() {
        return news_image_url;
    }

    public void setNews_image_url(String news_image_url) {
        this.news_image_url = news_image_url;
    }

    public String getNews_image_alt() {
        return news_image_alt;
    }

    public void setNews_image_alt(String news_image_alt) {
        this.news_image_alt = news_image_alt;
    }

    public String getNews_image_description() {
        return news_image_description;
    }

    public void setNews_image_description(String news_image_description) {
        this.news_image_description = news_image_description;
    }

    public String getNews_html_content() {
        return news_html_content;
    }

    public void setNews_html_content(String news_html_content) {
        this.news_html_content = news_html_content;
    }

    public String getNews_translate_en() {
        return news_translate_en;
    }

    public void setNews_translate_en(String news_translate_en) {
        this.news_translate_en = news_translate_en;
    }

    public String getNews_content_translate_en() {
        return news_content_translate_en;
    }

    public void setNews_content_translate_en(String news_content_translate_en) {
        this.news_content_translate_en = news_content_translate_en;
    }

    public String getNews_title_translate_en() {
        return news_title_translate_en;
    }

    public void setNews_title_translate_en(String news_title_translate_en) {
        this.news_title_translate_en = news_title_translate_en;
    }

    public String getNews_content_translate_cn() {
        return news_content_translate_cn;
    }

    public void setNews_content_translate_cn(String news_content_translate_cn) {
        this.news_content_translate_cn = news_content_translate_cn;
    }

    public String getNews_title_translate_cn() {
        return news_title_translate_cn;
    }

    public void setNews_title_translate_cn(String news_title_translate_cn) {
        this.news_title_translate_cn = news_title_translate_cn;
    }

    public Integer getNews_no() {
        return news_no;
    }

    public void setNews_no(Integer news_no) {
        this.news_no = news_no;
    }
}
