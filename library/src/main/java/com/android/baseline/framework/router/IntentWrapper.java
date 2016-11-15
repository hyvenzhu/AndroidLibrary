package com.android.baseline.framework.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.baseline.framework.router.annotations.ClassName;
import com.android.baseline.framework.router.annotations.RequestCode;
import com.android.baseline.framework.router.annotations.TargetClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Intent Wrapper
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/23 17:58]
 */

public class IntentWrapper {
    private Context mContext;
    private Bundle mExtras;
    private Class mTargetClass;
    private int mRequestCode = -1;
    private Intent mIntent;
    private Method mMethod;

    IntentWrapper(Context context, Class targetClass, int requestCode, Method method) {
        mContext = context;
        mTargetClass = targetClass;
        mRequestCode = requestCode;
        mMethod = method;

        mIntent = new Intent();
        mIntent.setClass(mContext, mTargetClass);
    }

    public Method getMethod() {
        return mMethod;
    }

    public Class getTargetClass() {
        return mTargetClass;
    }

    public void setClass(@NonNull String newClassName) throws ClassNotFoundException {
        setClass(Class.forName(newClassName));
    }

    public void setClass(@NonNull Class newTargetClass) {
        mTargetClass = newTargetClass;
        mIntent.setClass(mContext, mTargetClass);
    }

    public Bundle getExtras() {
        return mExtras;
    }

    public void setExtras(Bundle extras) {
        mExtras = extras;
        mIntent.putExtras(mExtras);
    }

    public Intent getIntent() {
        return mIntent;
    }

    public Context getContext() {
        return mContext;
    }

    public void addFlags(int flags) {
        mIntent.addFlags(flags);
    }

    public void start() {
        if (mRequestCode == -1) {
            startActivity();
        } else {
            startActivityForResult(mRequestCode);
        }
    }

    public void startActivity() {
        if (!(mContext instanceof Activity)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(mIntent);
    }

    public void startActivityForResult(int requestCode) {
        if (!(mContext instanceof Activity)) {
            throw new RuntimeException("startActivityForResult only works for activity context");
        }
        ((Activity) mContext).startActivityForResult(mIntent, requestCode);
    }

    public static final class Builder {
        private Context mContext;

        Method mMethod;
        String mClassName;
        Class mTargetClass;
        int mRequestCode;

        public Builder(Context context, Method method) {
            mContext = context;
            mMethod = method;
        }

        public IntentWrapper build() throws ClassNotFoundException {
            // 解析方法注解
            Annotation[] methodAnnotations = mMethod.getAnnotations();
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            if (TextUtils.isEmpty(mClassName) && mTargetClass == null) {
                throw new RuntimeException("Target Class is required.");
            }
            return new IntentWrapper(mContext, mTargetClass == null ? Class.forName(mClassName) : mTargetClass,
                    mMethod.isAnnotationPresent(RequestCode.class) ? mRequestCode : -1, mMethod);
        }

        /**
         * parse method annotation
         *
         * @param annotation
         * @return
         */
        void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof ClassName) {
                mClassName = ((ClassName) annotation).value();
            } else if (annotation instanceof TargetClass) {
                mTargetClass = ((TargetClass) annotation).value();
            } else if (annotation instanceof RequestCode) {
                mRequestCode = ((RequestCode) annotation).value();
            }
        }
    }
}
