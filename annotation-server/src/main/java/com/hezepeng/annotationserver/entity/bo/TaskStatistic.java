package com.hezepeng.annotationserver.entity.bo;


import lombok.Data;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/4/3 10:22
 * @description
 */
@Data
public class TaskStatistic {
    private Integer newsTotalCount;

    private Integer annotationDoneCount;

    private Integer waitCheckCount;

    private Integer waitArbitrateCount;

    private Integer waitAnnotationCount;
}
