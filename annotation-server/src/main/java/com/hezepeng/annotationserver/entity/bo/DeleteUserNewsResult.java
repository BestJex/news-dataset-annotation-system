package com.hezepeng.annotationserver.entity.bo;

import lombok.Data;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/4/3 13:48
 * @description
 */
@Data
public class DeleteUserNewsResult {
    private Integer deleteSimilarityAnnotationCount;

    private Integer initNewsCount;

}
