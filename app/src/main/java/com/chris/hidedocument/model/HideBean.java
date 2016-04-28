package com.chris.hidedocument.model;

import com.chris.hidedocument.listener.OnHideListener;

/**
 * Created by zoulx on 2016/3/23.
 */
public class HideBean{
    private OnHideListener onHideListener;
    private int id;

    public OnHideListener getOnHideListener() {
        return onHideListener;
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        this.onHideListener = onHideListener;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}