package com.android.baseline.framework.volley.plus.multipart;

import java.util.Random;

import org.apache.http.util.EncodingUtils;

import android.text.TextUtils;
/**
 * Boundary generator
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-22]
 */
public class Boundary
{
    /* The pool of ASCII chars to be used for generating a multipart boundary. */
    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();   //$NON-NLS-1$

    private final String boundary;
    private final byte[] startingBoundary;
    private final byte[] closingBoundary;
    
    public Boundary(String boundary) {
        if (TextUtils.isEmpty(boundary)) {
            boundary = generateBoundary();
        }
        this.boundary = boundary;
        
        final String starting = "--" + boundary + MultipartEntity.CRLF;         //$NON-NLS-1$
        final String closing  = "--" + boundary + "--" + MultipartEntity.CRLF;  //$NON-NLS-1$
        
        startingBoundary = EncodingUtils.getAsciiBytes(starting);
        closingBoundary  = EncodingUtils.getAsciiBytes(closing);
    }
    
    public String getBoundary() {
        return boundary;
    }

    byte[] getStartingBoundary() {
        return startingBoundary;
    }

    byte[] getClosingBoundary() {
        return closingBoundary;
    }
    
    private static String generateBoundary() {
        // Boundary delimiters must not appear within the encapsulated material, 
        // and must be no longer than 70 characters, not counting the two
        // leading hyphens.
        Random rand = new Random();
        final int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        StringBuilder buffer = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }
}
