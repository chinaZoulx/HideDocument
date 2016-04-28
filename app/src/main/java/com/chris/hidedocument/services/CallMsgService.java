package com.chris.hidedocument.services;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.util.Log;

import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.listener.OnHideSmsListener;
import com.chris.hidedocument.model.CallMsgBean;
import com.chris.hidedocument.tools.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by zoulx on 2016/4/9.
 */
public class CallMsgService {

    private static CallMsgService instance;
    public static final String smsTable = "call_table";
    private OnHideSmsListener mOnHideSmsListener;

    /**
     * 获取实例
     * @return
     */
    public static CallMsgService newInstance() {
        if (instance == null) {
            instance = new CallMsgService();
        }
        return instance;
    }

    private CallMsgService() {
    }


    /**
     * 读取通话记录
     * @param context
     * @return
     */
    public List<CallMsgBean> readCallMsg(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        List<CallMsgBean> callMsgBeanList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.e("TEST", cursor.getColumnName(i) + " : " + cursor.getString(i) + "\n");
            }
            do {
                CallMsgBean bean = new CallMsgBean();
                bean.setAddress(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                bean.setType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                bean.setDate(FormatUtils.getStandardDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)),"yyyy-MM-dd HH:mm:ss"));
                bean.setPerson(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                bean.setCallMsgId(cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID)));
                callMsgBeanList.add(bean);
            } while (cursor.moveToNext());
        }
        return callMsgBeanList;
    }


    /**
     * 隐藏通话记录
     * @param context
     * @param callMsgBeans
     * @return
     */
    public boolean hideCallMsg(Context context, List<CallMsgBean> callMsgBeans) {
        boolean flag = false;
        for (int i = 0; i < callMsgBeans.size(); i++) {
            CallMsgBean bean = callMsgBeans.get(i);
            if (bean.isCheck()) {
                int delFlag = deleteCallMsg(context, bean);
                if (delFlag == 0) {//机型问题
                    SmsService.showToast(context, "机型暂不支持隐藏短信");
                    continue;
                }
                if (delFlag != -1) {//删除成功
                    DBService.newInstance().saveCallMsg(bean);
                    callMsgBeans.remove(bean);
                    i--;
                    flag = true;
                }
            }
        }
        mOnHideSmsListener.onHideCallMsgFinish(callMsgBeans);
        return flag;
    }

    /**
     * 删除短信
     * @param context
     * @param callMsgBean
     * @return
     */
    private int deleteCallMsg(Context context, CallMsgBean callMsgBean) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(CallLog.Calls.CONTENT_URI, "_id=?", new String[]{callMsgBean.getCallMsgId()});
    }

    /**
     * 恢复通话记录
     * @param context
     * @param callMsgBean
     * @return
     */
    public boolean restoreCallMsg(Context context, CallMsgBean callMsgBean) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        //键字对的方式写入
        values.put(CallLog.Calls.DATE,FormatUtils.getStandardDate(callMsgBean.getDate(),"yyyy-MM-dd HH:mm:ss").getTime());
        values.put(CallLog.Calls.NUMBER, callMsgBean.getAddress());
        values.put(CallLog.Calls.TYPE, callMsgBean.getType());
        values.put(CallLog.Calls.CACHED_NAME, callMsgBean.getPerson());
        return contentResolver.insert(CallLog.Calls.CONTENT_URI, values) != null;
    }


    /**
     * 设置隐藏的监听
     * @param onHideSmsListener
     */
    public void setOnHideSmsListener(OnHideSmsListener onHideSmsListener) {
        if (onHideSmsListener != null) {
            this.mOnHideSmsListener = onHideSmsListener;
        }
    }
}
