package com.hiphonezhu.test.demo;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/27 09:36]
 */

public class UploadResult {
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "success='" + success + '\'' +
                '}';
    }
}
