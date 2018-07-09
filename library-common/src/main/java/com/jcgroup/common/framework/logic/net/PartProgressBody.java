package com.jcgroup.common.framework.logic.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * Part request body with progress feedback
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */

public abstract class PartProgressBody {
    public static RequestBody create(final MediaType contentType, final File file, final IProgress progress) {
        if (file == null) throw new NullPointerException("content == null");

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    // buffer
                    byte[] buffer = new byte[10240];
                    int len = -1;
                    // has write lenght
                    long current = 0;
                    while ((len = fis.read(buffer)) != -1) {
                        sink.write(buffer, 0, len);

                        // callback upload progress
                        if (progress != null) {
                            current += len;
                            progress.onProgress(current, contentLength());
                        }
                    }
                } finally {
                    Util.closeQuietly(fis);
                }
            }
        };
    }
}
