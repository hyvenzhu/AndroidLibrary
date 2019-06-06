package library.common.framework.ui.activity.view;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 子View视图控制器，用来更加细颗粒的拆分{@link AppDelegate}
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2019-1-23]
 */
public abstract class ViewController {
    protected AppDelegate mAppDelegate;
    View mView;

    public ViewController(@NonNull AppDelegate appDelegate, int parentId) {
        mAppDelegate = appDelegate;
        ViewGroup parent = appDelegate.getContentView().findViewById(parentId);
        mView = LayoutInflater.from(parent.getContext()).inflate(getView(), parent, true);
        ButterKnife.bind(this, parent);
        appDelegate.addViewController(this);
        initWidget();
    }

    /**
     * 返回View布局
     *
     * @return
     */
    public abstract @LayoutRes
    int getView();

    public void setVisibility(int visibility) {
        mView.setVisibility(visibility);
    }

    public void setOnClickListener(final View.OnClickListener listener, int... ids) {
        mAppDelegate.setOnClickListener(listener, ids);
    }

    public boolean isVisible() {
        return mAppDelegate.isVisible();
    }

    protected void initWidget() {
    }

    protected <T extends Activity> T getActivity() {
        return mAppDelegate.getActivity();
    }

    protected <T extends Fragment> T getFragment() {
        return mAppDelegate.getFragment();
    }

    protected Resources getResources() {
        return mAppDelegate.getResources();
    }

    protected String getString(@StringRes int strRes) {
        return mAppDelegate.getString(strRes);
    }

    protected String getString(@StringRes int strRes, Object... formatArgs) {
        return mAppDelegate.getString(strRes, formatArgs);
    }

    protected void showToast(CharSequence message) {
        mAppDelegate.showToast(message);
    }

    protected void showProgress(String message, boolean cancelable) {
        mAppDelegate.showProgress(message, cancelable);
    }

    protected void hideProgress() {
        mAppDelegate.hideProgress();
    }

    protected void onShow() {

    }

    protected void onHide() {

    }

    protected void onDestroy() {

    }
}
