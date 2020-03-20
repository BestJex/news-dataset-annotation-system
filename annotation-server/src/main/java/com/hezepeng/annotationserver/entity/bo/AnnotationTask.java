package com.hezepeng.annotationserver.entity.bo;

import com.hezepeng.annotationserver.entity.UserTask;
import lombok.Data;
import sun.print.PSPrinterJob;

import java.util.List;

/**
 * @author Hezepeng
 * @email hezepeng96@foxmail.com
 * @date 2020/3/16 23:43
 * @description
 */
@Data
public class AnnotationTask {

    private int newsCount;
    private int ratio;
    private List<UserTask> userTaskList;

    public AnnotationTask(int newsCount, int ratio, List<UserTask> userTaskList) {
        this.newsCount = newsCount;
        this.ratio = ratio;
        this.userTaskList = userTaskList;
    }
}
