package library.common.util;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 分割线
 *
 * @author zhuhf
 * @version 2020-03-27
 */
public class RecyclerViewDivider {
    private @ColorInt
    int dividerColor;
    private int dividerSize;
    private boolean hideLast;
    private int insetBefore;
    private int insetAfter;

    private RecyclerViewDivider(@ColorInt int dividerColor, int dividerSize, boolean hideLast, int insetBefore, int insetAfter) {
        this.dividerColor = dividerColor;
        this.dividerSize = dividerSize;
        this.hideLast = hideLast;
        this.insetBefore = insetBefore;
        this.insetAfter = insetAfter;
    }

    public static Builder with(Activity activity) {
        return new Builder();
    }

    public void addTo(RecyclerView rv) {
        rv.addItemDecoration(new LayoutManagerDivider(dividerColor, dividerSize, hideLast).inset(insetBefore, insetAfter));
    }

    public static class Builder {
        private @ColorInt
        int dividerColor = -1;
        private int dividerSize;
        private boolean hideLast;
        private int insetBefore;
        private int insetAfter;

        private Builder() {
        }

        public Builder color(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder size(int dividerSize) {
            this.dividerSize = dividerSize;
            return this;
        }

        public Builder hideLastDivider() {
            this.hideLast = true;
            return this;
        }

        public Builder inset(int insetBefore, int insetAfter) {
            this.insetBefore = insetBefore;
            this.insetAfter = insetAfter;
            return this;
        }

        public RecyclerViewDivider build() {
            return new RecyclerViewDivider(dividerColor == -1 ? Color.TRANSPARENT : dividerColor, dividerSize, hideLast, insetBefore, insetAfter);
        }
    }

    public class LayoutManagerDivider extends RecyclerView.ItemDecoration {
        private Paint paint = new Paint();
        private int dividerSize;
        private boolean hideLast;
        private int insetBefore;
        private int insetAfter;

        private LayoutManagerDivider(@ColorInt int dividerColor, int dividerSize, boolean hideLast) {
            super();
            paint.setColor(dividerColor);
            this.dividerSize = dividerSize;
            this.hideLast = hideLast;
        }

        public LayoutManagerDivider inset(int insetBefore, int insetAfter) {
            this.insetBefore = insetBefore;
            this.insetAfter = insetAfter;
            return this;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            int childPosition = parent.getChildAdapterPosition(view);
            final int childCount = parent.getAdapter().getItemCount();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

                int pos = parent.getChildAdapterPosition(view);
                int column = (pos) % gridLayoutManager.getSpanCount();

                int dividerWidthTop = dividerSize / 2;
                int dividerWidthBot = dividerSize - dividerWidthTop;

                outRect.top = dividerWidthTop;
                outRect.bottom = dividerWidthBot;

                outRect.left = (column * dividerSize / gridLayoutManager.getSpanCount());
                outRect.right = dividerSize - (column + 1) * dividerSize / gridLayoutManager.getSpanCount();
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (!hideLast || childPosition != childCount - 1) {
                    if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                        outRect.set(0, 0, 0, dividerSize);
                    } else {
                        outRect.set(0, 0, dividerSize, 0);
                    }
                }
            }
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            final int childCount = parent.getChildCount();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);

                    final int left = child.getLeft();
                    final int right = child.getRight();
                    final int top = child.getTop();
                    final int bottom = child.getBottom();

                    int pos = parent.getChildAdapterPosition(child);
                    int column = (pos) % gridLayoutManager.getSpanCount();

                    int dividerWidthTop = dividerSize / 2;
                    int dividerWidthBot = dividerSize - dividerWidthTop;

                    Rect rect = new Rect();
                    rect.top = dividerWidthTop;
                    rect.bottom = dividerWidthBot;
                    rect.left = (column * dividerSize / gridLayoutManager.getSpanCount());
                    rect.right = dividerSize - (column + 1) * dividerSize / gridLayoutManager.getSpanCount();
                    c.drawRect(left - rect.left, top - rect.top, right + rect.right, bottom + rect.bottom, paint);
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                for (int i = 0; i < childCount; i++) {
                    if (!hideLast || i != childCount - 1) {
                        final View child = parent.getChildAt(i);
                        final int left = child.getLeft();
                        final int top = child.getTop();
                        final int right = child.getRight();
                        final int bottom = child.getBottom();
                        if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                            c.drawRect(left + insetBefore, bottom, right - insetAfter, bottom + dividerSize, paint);
                        } else {
                            c.drawRect(right, top + insetBefore, right + dividerSize, bottom - insetAfter, paint);
                        }
                    }
                }
            }
        }
    }
}
