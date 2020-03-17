package com.hezepeng.annotationserver.entity.bo;

import com.hezepeng.annotationserver.entity.User;
import lombok.Data;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/13 18:11
 * @description
 */
@Data
public class UserBo {

    private User user;

    private Integer completeTaskCount = 0;

    private Integer revisionTaskCount = 0;

    private Integer undoTaskCount = 0;

    public UserBo(User user, Integer completeTaskCount, Integer revisionTaskCount, Integer undoTaskCount) {
        this.user = user;
        this.completeTaskCount = completeTaskCount;
        this.revisionTaskCount = revisionTaskCount;
        this.undoTaskCount = undoTaskCount;
    }
}
