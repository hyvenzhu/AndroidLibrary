package com.android.baseline.framework.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * author : zhuhf
 * e-mail : hiphonezhu@gmail.com
 * time   : 2018/03/02
 * desc   :
 * version: 1.0
 */
public interface Displayable {
    /**
     * 显示图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void display(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId);
    
    /**
     * 显示图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    void display(Context context, ImageView imageView, String url);
    
    /**
     * 显示圆形图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void displayCircle(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId);
    
    /**
     * 显示圆形图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    void displayCircle(Context context, ImageView imageView, String url);
    
    /**
     * 显示圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius        圆角半径
     * @param placeHolderId 占位图
     * @param errorHolderId 错误图
     */
    void displayRoundedCorners(Context context, ImageView imageView, String url, int radius, int placeHolderId, int errorHolderId);
    
    /**
     * 显示圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius    圆角半径
     */
    void displayRoundedCorners(Context context, ImageView imageView, String url, int radius);
}
