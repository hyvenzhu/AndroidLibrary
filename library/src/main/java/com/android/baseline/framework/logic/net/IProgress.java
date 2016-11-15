package com.android.baseline.framework.logic.net;

/**
 * 文件下载进度
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/23 10:11]
 */
public interface IProgress {
    /**
     * 进度
     *
     * @param current 已下载
     * @param total   总共大小
     */
    void onProgress(long current, long total);
}
