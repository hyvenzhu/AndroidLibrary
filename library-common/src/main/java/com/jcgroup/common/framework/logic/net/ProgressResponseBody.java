package com.jcgroup.common.framework.logic.net;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 带进度的响应
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-7-24]
 */
public class ProgressResponseBody extends ResponseBody {
    
    private BufferedSource bufferedSource;
    
    private ResponseBody responseBody;
    
    private WeakReference<IProgress> listener;
    
    public ProgressResponseBody(String url, ResponseBody responseBody) {
        this.responseBody = responseBody;
        listener = ProgressResponseInterceptor.LISTENER_MAP.get(url);
    }
    
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }
    
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }
    
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new ProgressSource(responseBody.source()));
        }
        return bufferedSource;
    }
    
    private class ProgressSource extends ForwardingSource {
        
        long totalBytesRead = 0;
        
        ProgressSource(Source source) {
            super(source);
        }
        
        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            final long fullLength = responseBody.contentLength();
            if (bytesRead == -1) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
            if (listener != null && listener.get() != null) {
                listener.get().onProgress(totalBytesRead, fullLength);
            }
            return bytesRead;
        }
    }
}
