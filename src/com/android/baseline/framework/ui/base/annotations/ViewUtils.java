package com.android.baseline.framework.ui.base.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.android.baseline.framework.ui.base.annotations.event.OnClick;
import com.android.baseline.framework.ui.base.annotations.event.OnItemClick;
import com.android.baseline.framework.ui.base.annotations.event.OnItemLongClick;
import com.android.baseline.framework.ui.base.annotations.event.OnLongClick;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;

/**
 * 利用注解实现View初始化和事件绑定
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class ViewUtils
{
    /**
     * 注解View
     * @param activity activity对象
     */
    public static void inject(Activity activity)
    {
        inject(activity, activity.getWindow().getDecorView());
    }
    
    /**
     * 注解View、事件
     * @param classObj class对象
     * @param contentView 父View对象
     */
    public static void inject(Object classObj, View contentView)
    {
        try
        {
            injectViews(classObj, contentView);
            injectListeners(classObj, contentView);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * View注解
     * @param classObj
     * @param contentView
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    private static void injectViews(Object classObj, View contentView) throws IllegalAccessException, IllegalArgumentException 
    {
        Class<?> clazz = classObj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(ViewInject.class))
            {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                int id = viewInject.value();
                if (id > 0)
                {
                    field.setAccessible(true);
                    field.set(classObj, contentView.findViewById(id));
                }
            }
        }
    }
    
    /**
     * 事件注解
     * @param classObj
     * @param contentView
     * @throws Exception 
     */
    private static void injectListeners(Object classObj, View contentView) throws Exception
    {
        Class<?> clazz = classObj.getClass();
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods)
        {
            if (method.isAnnotationPresent(OnClick.class))
            {
                setOnClickListener(classObj, contentView, method);
            }
            else if (method.isAnnotationPresent(OnLongClick.class))
            {
                setOnLongClickListener(classObj, contentView, method);
            }
            else if (method.isAnnotationPresent(OnItemClick.class))
            {
                setOnItemClickListener(classObj, contentView, method);
            }
            else if (method.isAnnotationPresent(OnItemLongClick.class))
            {
                setOnItemLongClickListener(classObj, contentView, method);
            }
        }
    }
    
    /**
     * 单击事件绑定
     * @param classObj
     * @param contentView
     * @param method
     */
    private static void setOnClickListener(Object classObj, View contentView, Method method)
    {
        OnClick onclick = method.getAnnotation(OnClick.class);
        int[] ids = onclick.value();
        if (ids != null && ids.length > 0)
        {
            for (int id : ids)
            {
                View view = contentView.findViewById(id);
                view.setOnClickListener(new EventListener(classObj, method.getName()));
            }
        }
    }
    
    /**
     * 长按事件绑定
     * @param classObj
     * @param contentView
     * @param method
     */
    private static void setOnLongClickListener(Object classObj, View contentView, Method method)
    {
        OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
        int[] ids = onLongClick.value();
        if (ids != null && ids.length > 0)
        {
            for (int id : ids)
            {
                View view = contentView.findViewById(id);
                view.setOnLongClickListener(new EventListener(classObj, method.getName()));
            }
        }
    }
    
    /**
     * Item单击事件
     * @param classObj
     * @param contentView
     * @param method
     */
    private static void setOnItemClickListener(Object classObj, View contentView, Method method)
    {
        OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
        int[] ids = onItemClick.value();
        if (ids != null && ids.length > 0)
        {
            for (int id : ids)
            {
                View view = contentView.findViewById(id);
                if (view instanceof AbsListView)
                {
                    ((AbsListView) view).setOnItemClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }
    
    /**
     * Item长按事件
     * @param classObj
     * @param contentView
     * @param method
     */
    private static void setOnItemLongClickListener(Object classObj, View contentView, Method method)
    {
        OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
        int[] ids = onItemLongClick.value();
        if (ids != null && ids.length > 0)
        {
            for (int id : ids)
            {
                View view = contentView.findViewById(id);
                if (view instanceof AbsListView)
                {
                    ((AbsListView) view).setOnItemLongClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }
}
