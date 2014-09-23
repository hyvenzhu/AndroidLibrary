package com.android.baseline.framework.volley.multipart;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;
/**
 * URL encode util
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-22]
 */
public class UrlEncodingHelper
{
    public static String encode(final String content, final String encoding) {
        try {
            return URLEncoder.encode(
                content, 
                encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET
            );
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }
}
