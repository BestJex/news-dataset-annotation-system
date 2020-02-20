package com.hezepeng.annotationserver.entity;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 17:19
 */
public class Author {
    private String name;
    private String author_url;

    public Author(String name, String author_url) {
        this.name = name;
        this.author_url = author_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }
}
