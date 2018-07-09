package com.jcgroup.common.framework.task;

/**
 * 任务接口定义
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public interface ITask {
    /**
     * 执行耗时任务
     *
     * @return
     */
    Object doInBackground();
}
