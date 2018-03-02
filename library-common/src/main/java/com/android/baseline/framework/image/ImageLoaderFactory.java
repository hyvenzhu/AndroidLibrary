package com.android.baseline.framework.image;

/**
 * author : zhuhf
 * e-mail : hiphonezhu@gmail.com
 * time   : 2018/03/02
 * desc   :
 * version: 1.0
 */
public class ImageLoaderFactory {
    
    public static Displayable createDefault() {
        return create(null);
    }
    
    public static Displayable create(String type) {
        switch (type) {
            default:
                return new GlideDisplayable();
        }
    }
}
