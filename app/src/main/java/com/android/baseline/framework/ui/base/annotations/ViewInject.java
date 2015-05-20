package com.android.baseline.framework.ui.base.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * View注解定义
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-10]
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject
{
    // R资源
    int value() default 0;
}
