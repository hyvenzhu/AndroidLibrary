package com.android.baseline.framework.volley.multipart;
/**
 * Progress call back interface
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-22]
 */
public interface ProgressListener
{
    /**
     * Callback method thats called on each byte transfer.
     */
    void onProgress(String key, long transferredBytes, long totalSize);
}
