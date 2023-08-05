package library.common.framework.ui.activity.presenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import library.common.framework.ui.activity.view.IDelegate;
import library.common.util.Callback;

/**
 * Presenter base class for DialogFragment
 * Presenter层的实现基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class DialogPresenter<T extends IDelegate> extends DialogFragment{
    public T viewDelegate;

    Callback callback;

    public <T> void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public <T> void doCall(T data) {
        if (callback != null) {
            callback.call(data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onShow();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewDelegate.onHide();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDelegate.create(this, inflater, container, savedInstanceState);
        return viewDelegate.getContentView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isDestroyed = false;
        viewDelegate.initWidget(getArguments());
        viewDelegate.initChildControllers();
        onCreate();
    }

    protected void onCreate() {
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
        viewDelegate = null;

        isDestroyed = true;
    }

    protected abstract Class<T> getDelegateClass();

    boolean isDestroyed;
}
