package com.hezepeng.annotationserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 15:09
 * 存储每个集合的自增id的信息
 */

@Document(collection = "Annotation_System_IncrementInfo")
@Data
public class IncrementInfo {
    @Id
    private String id;

    private String collName;

    private Integer incId;
}
