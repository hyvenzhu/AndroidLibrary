package library.common.framework.ui.activity.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
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
    void create(@NonNull Context context, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    
    void create(@NonNull Fragment fragment, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    void initWidget();

    void initWidget(@NonNull Intent intent);

    void initWidget(@Nullable Bundle args);

    void initChildControllers();

    void onShow();
    
    void onHide();
    
    boolean isVisible();
    
    void onDestroy();
    
    /**
     * 返回 Activity、Fragment、Dialog 的内容布局
     * @return
     */
    ViewGroup getContentView();
}
