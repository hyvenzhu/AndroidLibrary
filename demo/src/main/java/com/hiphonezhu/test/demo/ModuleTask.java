package com.hiphonezhu.test.demo;

import com.android.baseline.framework.asyncquery.Task;
import com.android.baseline.framework.logic.InfoResult;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 17:03]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ModuleTask extends Task {
    public ModuleTask(int taskId, Object subscriber) {
        super(taskId, subscriber);
    }

    @Override
    public Object doInBackground() {
        try {
            Thread.sleep(6 *1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new InfoResult.Builder().success(true).extraObj("task run over").build();
    }
}
