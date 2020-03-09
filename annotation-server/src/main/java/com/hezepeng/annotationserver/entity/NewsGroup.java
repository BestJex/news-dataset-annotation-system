package com.hezepeng.annotationserver.entity;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/2/28 15:39
 * @description
 */

@Data
@ToString
@Document("News_Group")
public class NewsGroup {
    @Id
    private ObjectId _id;

    private String news_id_1;

    private String news_id_2;

    private boolean is_same_event;

}
