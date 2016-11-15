package com.android.baseline.framework.ui.activity.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;

import com.android.baseline.framework.ui.activity.annotations.event.OnCheckedChange.OnCheckedChanged;
import com.android.baseline.framework.ui.activity.annotations.event.OnClick;
import com.android.baseline.framework.ui.activity.annotations.event.OnItemClick;
import com.android.baseline.framework.ui.activity.annotations.event.OnItemLongClick;
import com.android.baseline.framework.ui.activity.annotations.event.OnLongClick;

/**
 * 利用注解实现View初始化和事件绑定
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class ViewUtils {
    /**
     * 注解View
     *
     * @param activity activity对象
     */
    public static void inject(Activity activity) {
        inject(activity, new ViewFinder(activity));
    }

    /**
     * 注解View、事件
     *
     * @param classObj    class对象
     * @param contentView 父View对象
     */
    public static void inject(Object classObj, View contentView) {
        inject(classObj, new ViewFinder(contentView));
    }

    private static void inject(Object classObj, ViewFinder finder) {
        try {
            injectViews(classObj, finder);
            injectListeners(classObj, finder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * View注解
     *
     * @param classObj
     * @param viewFinder
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static void injectViews(Object classObj, ViewFinder viewFinder) throws IllegalAccessException, IllegalArgumentException {
        Class<?> clazz = classObj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ViewInject.class)) {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                int id = viewInject.value();
                if (id > 0) {
                    field.setAccessible(true);
                    View v = viewFinder.findViewById(id);
                    if (v != null) {
                        try {
                            field.set(classObj, v);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 事件注解
     *
     * @param classObj
     * @param viewFinder
     * @throws Exception
     */
    private static void injectListeners(Object classObj, ViewFinder viewFinder) throws Exception {
        Class<?> clazz = classObj.getClass();
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            try {
                if (method.isAnnotationPresent(OnClick.class)) {
                    setOnClickListener(classObj, viewFinder, method);
                } else if (method.isAnnotationPresent(OnLongClick.class)) {
                    setOnLongClickListener(classObj, viewFinder, method);
                } else if (method.isAnnotationPresent(OnItemClick.class)) {
                    setOnItemClickListener(classObj, viewFinder, method);
                } else if (method.isAnnotationPresent(OnItemLongClick.class)) {
                    setOnItemLongClickListener(classObj, viewFinder, method);
                } else if (method.isAnnotationPresent(OnCheckedChanged.class)) {
                    setOnCheckedChangeListener(classObj, viewFinder, method);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单击事件绑定
     *
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnClickListener(Object classObj, ViewFinder viewFinder, Method method) {
        OnClick onclick = method.getAnnotation(OnClick.class);
        int[] ids = onclick.value();
        if (ids != null) {
            for (int id : ids) {
                View view = viewFinder.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * 长按事件绑定
     *
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnLongClickListener(Object classObj, ViewFinder viewFinder, Method method) {
        OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
        int[] ids = onLongClick.value();
        if (ids != null) {
            for (int id : ids) {
                View view = viewFinder.findViewById(id);
                if (view != null) {
                    view.setOnLongClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * Item单击事件
     *
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnItemClickListener(Object classObj, ViewFinder viewFinder, Method method) {
        OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
        int[] ids = onItemClick.value();
        if (ids != null) {
            for (int id : ids) {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof AbsListView) {
                    ((AbsListView) view).setOnItemClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * Item长按事件
     *
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnItemLongClickListener(Object classObj, ViewFinder viewFinder, Method method) {
        OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
        int[] ids = onItemLongClick.value();
        if (ids != null) {
            for (int id : ids) {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof AbsListView) {
                    ((AbsListView) view).setOnItemLongClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * checkbox选中事件
     *
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnCheckedChangeListener(Object classObj, ViewFinder viewFinder, Method method) {
        OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
        int[] ids = onCheckedChanged.value();
        if (ids != null) {
            for (int id : ids) {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof CompoundButton) {
                    ((CompoundButton) view).setOnCheckedChangeListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }
}
