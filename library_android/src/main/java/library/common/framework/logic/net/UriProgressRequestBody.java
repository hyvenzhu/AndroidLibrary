package library.common.framework.logic.net;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 带进度的附件上传
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2022-09-11]
 */

public class UriProgressRequestBody extends RequestBody {

    private final Context context;
    private final MediaType contentType;
    private final Uri uri;
    private final WeakReference<IProgress> listener;

    private long contentLength = 0;

    public UriProgressRequestBody(final Context context, final MediaType contentType, final Uri uri, final IProgress progress) {
        this.context = context.getApplicationContext();
        this.contentType = contentType;
        this.uri = uri;
        listener = new WeakReference<>(progress);

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            contentLength = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(is);
        }
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
        Source source = null;
        try {
            long totalBytesWrite = 0;
            InputStream is = context.getContentResolver().openInputStream(uri);
            source = Okio.source(is);
            long len;
            while ((len = source.read(sink.buffer(), 10240)) != -1) {
                sink.flush();

                if (listener != null && listener.get() != null) {
                    totalBytesWrite += len;
                    listener.get().onProgress(totalBytesWrite, contentLength);
                }
            }
        } finally {
            Util.closeQuietly(source);
        }
    }
}
