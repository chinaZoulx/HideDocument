package com.chris.hidedocument.listener;

import com.chris.hidedocument.model.CallMsgBean;
import com.chris.hidedocument.model.SmsBean;

import java.util.List;

/**
 * Created by zoulx on 2016/3/30.
 */
public abstract class OnHideSmsListener {

    public void onHideSmsFinish(List<SmsBean> smsBeanList) {
    }

    public void onHideCallMsgFinish(List<CallMsgBean> callMsgBeanList) {
    }
}
