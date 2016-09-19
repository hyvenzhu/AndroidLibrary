package com.android.baseline.framework.ui.activity.annotations.event;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 一句话功能简述<p>
 * 复选框选择事件
 * @author 李瑞亮
 * @version [OApp, 2014-10-29]
 */
public class OnCheckedChange
{
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnCheckedChanged
    {
        int[] value();
    }
}
