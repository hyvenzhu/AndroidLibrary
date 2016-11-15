package com.android.baseline.framework.router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Target ClassName Annotation
 * 适合跨团队Class未知的情况, 已知{@link TargetClass}
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 11:30]
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassName {
    String value();
}
