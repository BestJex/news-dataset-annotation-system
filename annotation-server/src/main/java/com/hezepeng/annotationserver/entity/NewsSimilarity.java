package com.hezepeng.annotationserver.entity;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/29 23:43
 * @description
 */
@Data
@ToString
@Document(collection = "News_Similarity")
public class NewsSimilarity {
    @Id
    @Field("_id")
    private ObjectId _id;

    private String username;

    private String id_A;
    private String id_B;
    private Integer time_A;
    private Integer time_B;
    private String is_related;
    private String relation;
}
