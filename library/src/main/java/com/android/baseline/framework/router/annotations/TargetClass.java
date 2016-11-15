package com.android.baseline.framework.router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Target Class Annotation
 * 与{@link ClassName}注解区别是: TargetClass目标Class已知
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 11:30]
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetClass {
    Class value();
}
