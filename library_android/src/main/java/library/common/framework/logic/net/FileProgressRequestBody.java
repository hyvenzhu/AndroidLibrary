package library.common.framework.logic.net;

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
 * @version [AndroidLibrary, 2018-3-6]
 */

public class FileProgressRequestBody extends RequestBody {
    
    private final MediaType contentType;
    private final File file;
    private final WeakReference<IProgress> listener;
    
    private final long contentLength;
    
    public FileProgressRequestBody(final MediaType contentType, final File file, final IProgress progress) {
        this.contentType = contentType;
        this.file = file;
        contentLength = file.length();
        listener = new WeakReference<>(progress);
    }
    
    @Override
    public MediaType contentType() {
        return contentType;
    }
    
    @Override
    public long contentLength() {
        return contentLength;
    }
    
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        FileInputStream fis = null;
        try {
            long totalBytesWrite = 0;
            fis = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int len;
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
