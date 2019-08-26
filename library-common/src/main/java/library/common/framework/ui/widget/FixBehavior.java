package library.common.framework.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * 1、解决滑动抖动问题；
 * 2、CoordinatorLayout刷新，ViewPager - Fragment - RecyclerView 场景，AppBarLayout 无法滚动问题（偶发）；
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-21]
 */
public class FixBehavior extends AppBarLayout.Behavior {

    public FixBehavior() {
        super();
    }

    public FixBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 方法一：setDragCallback 返回 true，详见 {@link android.support.design.widget.HeaderBehavior#canDragView(View)}
         */
        setDragCallback(new DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
        /**
         * 方法二：重写 {@link RecyclerView#isShown()}，返回 true
         */
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (ev.getAction() == ACTION_DOWN) {
            Object scroller = getSuperSuperField(this, "mScroller");
            if (scroller != null && scroller instanceof OverScroller) {
                OverScroller overScroller = (OverScroller) scroller;
                overScroller.abortAnimation();
            }
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private Object getSuperSuperField(Object paramClass, String paramString) {
        Object object = null;
        try {
            Field field = paramClass.getClass().getSuperclass().getSuperclass().getDeclaredField(paramString);
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}