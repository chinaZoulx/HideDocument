package com.chris.hidedocument.listener;

import com.chris.hidedocument.model.FileInfoBean;

import java.util.List;

/**
 * Created by zoulx on 2016/3/24.
 */
public abstract class OnSearchFileListener {

    public abstract void onSearchFinish(List<FileInfoBean> fileInfoBeanList);
    public void onFinish(){}
}
