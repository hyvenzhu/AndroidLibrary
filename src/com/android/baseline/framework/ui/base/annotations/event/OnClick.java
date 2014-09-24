package com.android.baseline.framework.ui.base.annotations.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 单击事件注解
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-19]
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick
{
    int[] value();
}
