package com.android.baseline.framework.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewGroupLayout {
    private View view;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    private View currentView;
    
    public ViewGroupLayout(View view) {
        super();
        this.view = view;
    }
    
    private void init() {
        params = view.getLayoutParams();
        if (view.getParent() != null) {
            parentView = (ViewGroup) view.getParent();
        } else {
            parentView = (ViewGroup) view.getRootView().findViewById(
                    android.R.id.content);
        }
        int count = parentView.getChildCount();
        for (int index = 0; index < count; index++) {
            if (view == parentView.getChildAt(index)) {
                viewIndex = index;
                break;
            }
        }
        currentView = view;
    }
    
    public View getCurrentLayout() {
        return currentView;
    }
    
    public void resetView() {
        showLayout(view);
    }
    
    public void showLayout(View view) {
        if (parentView == null) {
            init();
        }
        this.currentView = view;
        if (parentView.getChildAt(viewIndex) != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            parentView.removeViewAt(viewIndex);
            parentView.addView(view, viewIndex, params);
        }
    }
    
    public void showLayout(int layoutId) {
        showLayout(inflate(layoutId));
    }
    
    public View inflate(int layoutId) {
        return LayoutInflater.from(view.getContext()).inflate(layoutId, null);
    }
    
    public Context getContext() {
        return view.getContext();
    }
    
    public View getView() {
        return view;
    }
}
