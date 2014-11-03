package com.android.baseline.framework.ui.base.annotations;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 自定义的View事件 [触发的方法中反射用户的方法]
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class EventListener implements OnClickListener , OnLongClickListener , OnItemClickListener ,
        OnItemLongClickListener,OnCheckedChangeListener
{
    private Object receiver;
    private String methodName;

    public EventListener(Object receiver, String methodName)
    {
        this.receiver = receiver;
        this.methodName = methodName;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        try
        {
            Method method = receiver.getClass().getDeclaredMethod(methodName,
                    AdapterView.class,
                    View.class,
                    int.class,
                    long.class);
            return Boolean.valueOf(method.invoke(receiver,
                    arg0,
                    arg1,
                    arg2,
                    arg3).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        try
        {
            Method method = receiver.getClass().getDeclaredMethod(methodName,
                    AdapterView.class,
                    View.class,
                    int.class,
                    long.class);
            method.invoke(receiver,
                    arg0,
                    arg1,
                    arg2,
                    arg3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        try
        {
            Method method = receiver.getClass().getDeclaredMethod(methodName,
                    View.class);
            return Boolean.valueOf(method.invoke(receiver,
                    v).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            Method method = receiver.getClass().getDeclaredMethod(methodName,
                    View.class);
            method.invoke(receiver,
                    v);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        try
        {
            Method method = receiver.getClass().getDeclaredMethod(methodName,CompoundButton.class,
                    boolean.class);
            method.invoke(receiver,buttonView,isChecked);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
