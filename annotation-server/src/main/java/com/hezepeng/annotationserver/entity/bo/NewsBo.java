package com.hezepeng.annotationserver.entity.bo;

import lombok.Data;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/9 19:37
 * @description
 */
@Data
public class NewsBo {
    private String id;
    private Integer state;

    public NewsBo(String id, Integer state) {
        this.id = id;
        this.state = state;
    }
}
