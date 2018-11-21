
package library.common.framework.ui.swipeback.app;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import library.common.framework.ui.swipeback.SwipeBackLayout;
import library.common.framework.ui.swipeback.Utils;
import library.common.util.APKUtils;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        getSwipeBackLayout().addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                APKUtils.hideSoftInputFromWindow(SwipeBackActivity.this);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    public void addSwipeListener(SwipeBackLayout.SwipeListener swipeListener) {
        getSwipeBackLayout().addSwipeListener(swipeListener);
    }

    public void setEnableSwipeBackGesture(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    public void setScrimColor(@ColorInt int color) {
        getSwipeBackLayout().setScrimColor(color);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
