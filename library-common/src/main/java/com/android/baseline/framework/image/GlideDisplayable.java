package com.android.baseline.framework.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * author : zhuhf
 * e-mail : hiphonezhu@gmail.com
 * time   : 2018/03/02
 * desc   :
 * version: 1.0
 */
public class GlideDisplayable implements Displayable {
    
    @Override
    public void display(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeHolderId)
                .error(errorHolderId);
        Glide.with(context).load(url).apply(requestOptions).into(new DrawableImageViewTarget(imageView));
    }
    
    @Override
    public void display(Context context, ImageView imageView, String url) {
        display(context, imageView, url, -1, -1);
    }
    
    @Override
    public void displayCircle(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeHolderId)
                .error(errorHolderId)
                .circleCrop();
        Glide.with(context).load(url).apply(requestOptions).into(imageView);
    }
    
    @Override
    public void displayCircle(Context context, ImageView imageView, String url) {
        displayCircle(context, imageView, url, -1, -1);
    }
    
    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, String url, int radius, int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeHolderId)
                .error(errorHolderId)
                .transform(new RoundedCornersTransformation(radius, 0));
        Glide.with(context).load(url).apply(requestOptions).into(new DrawableImageViewTarget(imageView));
    }
    
    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, String url, int radius) {
        displayRoundedCorners(context, imageView, url, radius, -1, -1);
    }
}
