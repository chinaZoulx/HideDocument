package com.chris.hidedocument.listener;

import com.chris.hidedocument.model.FileInfoBean;

import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public abstract class OnHideListener {

    public abstract void onHideFinish(List<FileInfoBean> fileInfoBeanList);
}
