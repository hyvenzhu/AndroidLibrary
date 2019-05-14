package library.common.framework.ui.activity.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * View delegate base class
 * 视图层代理的接口协议
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public interface IDelegate {
    void create(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    
    void create(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void initWidget();

    void initWidget(Intent intent);

    void initWidget(Bundle args);

    void onShow();
    
    void onHide();
    
    boolean isVisible();
    
    void onDestroy();
    
    int getOptionsMenuId();
    
    Toolbar getToolbar();
    
    /**
     * 返回 Activity、Fragment、Dialog 的内容布局
     * @return
     */
    ViewGroup getContentView();
}
