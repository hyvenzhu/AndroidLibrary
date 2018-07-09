package com.jcgroup.common.framework.ui.adapter.page;

/**
 * 分页策略2: startIndex, endIndex
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public abstract class Page2 extends IPage {
    @Override
    public int handlePageIndex(int currPageIndex, int pageSize) {
        // 加载第一页数据(防止第一页使用"上拉加载更多")
        if (currPageIndex == getStartPageIndex() - 1)
        {
            return getStartPageIndex();
        }
        return currPageIndex + pageSize;
    }

    @Override
    protected int handlePage(int currPageIndex, int pageSize) {
        return currPageIndex + pageSize - 1;
    }

    /**
     * 起始下标递减
     */
    @Override
    public void decreaseStartIndex() {
        currPageIndex--;
        checkBound();
    }

    /**
     * 起始下标递减
     * @param size
     */
    @Override
    public void decreaseStartIndex(int size) {
        currPageIndex -= size;
        checkBound();
    }

    /**
     * 边界检测
     */
    private void checkBound() {
        if (currPageIndex < getStartPageIndex() - pageSize) {
            currPageIndex = getStartPageIndex() - pageSize;
        }
    }
}
