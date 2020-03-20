package com.hezepeng.annotationserver.entity;

import lombok.Data;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/19 12:51
 * @description
 */
@Data
public class UserTask {
    private int taskCount;
    private String username;
    private String name;

    public UserTask(int taskCount, String username, String name) {
        this.taskCount = taskCount;
        this.username = username;
        this.name = name;
    }
}
