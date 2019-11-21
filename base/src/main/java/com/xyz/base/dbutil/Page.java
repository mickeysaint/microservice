package com.xyz.base.dbutil;

import java.util.List;

public class Page<T> {

    private long count = 0;

    private List<T> dataList = null;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
