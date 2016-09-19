package com.android.baseline.framework.ui.activity.annotations;

import android.app.Activity;
import android.view.View;
/**
 * View查找器
 * @author hiphonezhu@gmail.com
 * @version [OApp, 2014-10-31]
 */
public class ViewFinder
{
    private View view;
    private Activity activity;
    
    public ViewFinder(Activity activity)
    {
        this.activity = activity;
    }
    
    public ViewFinder(View view)
    {
        this.view = view;
    }
    
    /**
     * 根据id查找 view
     * @param id
     * @return
     */
    public View findViewById(int id)
    {
        return activity != null? activity.findViewById(id) : view.findViewById(id);
    }
}
