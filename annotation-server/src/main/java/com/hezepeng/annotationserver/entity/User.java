package com.hezepeng.annotationserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hezepeng.annotationserver.annotation.AutoIncKey;
import com.hezepeng.annotationserver.common.Const;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 14:57
 */

//保证序列化json的时候,如果是null的对象,key也会消失，只作用于该类的第一层属性本身
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
@Document("Annotation_System_User")
public class User {
    @Id
    @AutoIncKey
    private Integer userId;

    private String openId;

    private String username;

    /**
     * 指定字段只在序列化时忽略、反序列化时保留
     * 该注解可以让对象序列号成json过程中过滤掉Password字段，但json反序列化成对象时依旧会生成Password字段
     */
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    private List<String> role;

    private String avatar;

    public User(String username, String password, List<String> role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = "";
        this.email = null;
        this.lastLoginTime = null;
        this.avatar = Const.DEFAULT_AVATAR;
    }

    @PersistenceConstructor
    public User(Integer userId, String openId, String username, String password, String name, String email, Date lastLoginTime, List<String> role, String avatar) {
        this.userId = userId;
        this.openId = openId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.lastLoginTime = lastLoginTime;
        this.role = role;
        this.avatar = avatar;
    }

    public User() {
    }
}
