package com.jcgroup.common.framework.logic.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * 带进度的附件上传
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */

public class ProgressRequestBody extends RequestBody {
    
    private MediaType contentType;
    
    private File file;
    
    private WeakReference<IProgress> listener;
    
    private long totalBytesWrite = 0;
    private long fullLength;
    
    public ProgressRequestBody(final MediaType contentType, final File file, final IProgress progress) {
        this.contentType = contentType;
        this.file = file;
        fullLength = file.length();
        listener = new WeakReference<>(progress);
    }
    
    @Override
    public MediaType contentType() {
        return contentType;
    }
    
    @Override
    public long contentLength() {
        return fullLength;
    }
    
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // buffer
            byte[] buffer = new byte[10240];
            int len;
            // has write length
            while ((len = fis.read(buffer)) != -1) {
                sink.write(buffer, 0, len);
                
                if (listener != null && listener.get() != null) {
                    totalBytesWrite += len;
                    listener.get().onProgress(totalBytesWrite, contentLength());
                }
            }
        } finally {
            Util.closeQuietly(fis);
        }
    }
}
