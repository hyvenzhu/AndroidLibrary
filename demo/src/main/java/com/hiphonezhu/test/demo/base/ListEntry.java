package com.hiphonezhu.test.demo.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Response类型为集合
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/06/24 10:30]
 */
public class ListEntry<T> {
    private int count;

    @JSONField(name = "rows") // 换成接口实际返回的名称
    private List<T> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
