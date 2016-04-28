package com.chris.hidedocument.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.listener.OnHideSmsListener;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.tools.FormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/3/25.
 */
public class SmsService {

    private static SmsService instance;

    private OnHideSmsListener mOnHideSmsListener;

    public static SmsService newInstance() {
        if (instance == null) {
            instance = new SmsService();
        }
        return instance;
    }

    /**
     * 写入短信
     *
     * @param context
     * @return
     */
    public boolean writeSms(Context context, SmsBean bean) {
        ContentResolver mContentResolver = context.getContentResolver();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("address", bean.getSmsAddress());
        mContentValues.put("body", bean.getSmsBody());
        mContentValues.put("type", bean.getSmsType());
        mContentValues.put("date", FormatUtils.getStandardDate(bean.getSmsDate(),"yyyy-MM-dd HH:mm:ss").getTime());
        mContentValues.put("person", bean.getSmsPerson());
        return mContentResolver.insert(Uri.parse("content://sms/inbox"), mContentValues) != null;
    }

    /**
     * 读取短信
     *
     * @param activity
     * @return
     */
    public List<SmsBean> readSms(Activity activity) {
        List<SmsBean> smsBeanList = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        Cursor mCursor = activity.managedQuery(uri, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                String info = "";
                for (int i = 0; i < mCursor.getColumnCount(); i++) {
                    info += "name:" + mCursor.getColumnName(i) + "=" + mCursor.getString(i) + ",";
                }
                Log.e("====>", info);
                //
                SmsBean bean = new SmsBean();
                bean.setSmsPerson(mCursor.getString(mCursor.getColumnIndex("person")));
                bean.setSmsAddress(mCursor.getString(mCursor.getColumnIndex("address")));
                long date=mCursor.getLong(mCursor.getColumnIndex("date"));
                bean.setSmsDate(FormatUtils.getStandardDate(date,"yyyy-MM-dd HH:mm:ss"));
                bean.setSmsBody(mCursor.getString(mCursor.getColumnIndex("body")));
                bean.setSmsType(mCursor.getString(mCursor.getColumnIndex("type")));
                bean.setSmsID(mCursor.getString(mCursor.getColumnIndex("_id")));
                bean.setSmsThreadID(mCursor.getString(mCursor.getColumnIndex("thread_id")));
                smsBeanList.add(bean);
            } while (mCursor.moveToNext());
        }
        return smsBeanList;
    }

    /**
     * 批量删除短信
     *
     * @param context
     * @param smsBeanList
     * @return
     */
    public List<SmsBean> deleteSmss(Context context, List<SmsBean> smsBeanList) {
        for (int i = 0; i < smsBeanList.size(); i++) {
            SmsBean bean = smsBeanList.get(i);
            if (bean.isCheck()) {
                String uriContent = "content://sms/";
                int result = context.getContentResolver().delete(Uri.parse(uriContent), "thread_id=" + bean.getSmsThreadID(), null);
                if (result == 0) {//机型问题
                    showToast(context, "机型暂不支持隐藏短信");
                    continue;
                }
                if (result != -1 && result > 0) {//删除成功
                    if (DBService.newInstance().saveSms(bean)) {
                        smsBeanList.remove(bean);
                        i--;
                    }
                }
            }
        }
        if(mOnHideSmsListener!=null) {
            //回调到原处
            mOnHideSmsListener.onHideSmsFinish(smsBeanList);
        }
        return smsBeanList;
    }

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public void setOnHideSmsListener(OnHideSmsListener onHideSmsListener) {
        if (onHideSmsListener != null) {
            this.mOnHideSmsListener = onHideSmsListener;
        }
    }
}
