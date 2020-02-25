package com.ahjrlc.common;

import java.util.ArrayList;

/**
 * 增强Arraylist的用法
 *
 * @author aachen0
 */
public class ArrayListPlus<E> extends ArrayList<E> {
    /**
     * 分页时存放分页前记录数，和pagehelper中的total兼容
     */
    private long total;

    public ArrayListPlus<E> append(E e) {
        super.add(e);
        return this;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
