package library.common.framework.ui.activity.presenter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import library.common.framework.ui.activity.view.IDelegate;
import library.common.framework.ui.swipeback.app.SwipeBackActivity;

/**
 * Presenter base class for Activity
 * Presenter层的实现基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class ActivityPresenter<T extends IDelegate> extends SwipeBackActivity {
    @NonNull
    public T viewDelegate;

    public ActivityPresenter() {
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("create IDelegate error");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.create(this, getLayoutInflater(), null, savedInstanceState);
        setContentView(viewDelegate.getContentView());
        isDestroyed = false;
        viewDelegate.initWidget(getIntent());
        viewDelegate.initChildControllers();
        onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onShow();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewDelegate.onHide();
    }

    protected void onCreate() {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
        viewDelegate = null;

        isDestroyed = true;
    }

    protected abstract Class<T> getDelegateClass();

    public boolean isDestroyed;

    public <R> void observe(final LiveData<R> liveData, final Observer<R> observer) {
        liveData.observe(this, new Observer<R>() {
            @Override
            public void onChanged(R r) {
                observer.onChanged((R) beforeOnChanged(r));
            }
        });
    }

    protected abstract Object beforeOnChanged(Object res);
}
