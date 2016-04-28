package com.chris.hidedocument.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.model.CallMsgBean;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.tools.FormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zoulx on 2016/4/9.
 */
public class DBService {

    private static DBService instance;
    private MyDatabaseHelper DBHepler;
    private ContentValues values;
    private SQLiteDatabase db;

    public static DBService newInstance() {
        if (instance == null) {
            instance = new DBService();
        }
        return instance;
    }

    public void init(Context context) {
        DBHepler = new MyDatabaseHelper(context, MainApplication.dbName, null, 1);
        values = new ContentValues();
        db = DBHepler.getWritableDatabase();
    }

    private DBService() {
    }

    /**
     * 保存短信
     * @param bean
     * @return
     */
    public boolean saveSms(SmsBean bean) {
        values.clear();
        values.put("person", bean.getSmsPerson());
        values.put("date", bean.getSmsDate());
        values.put("address", bean.getSmsAddress());
        values.put("type", bean.getSmsType());
        values.put("body", bean.getSmsBody());
        return db.insert("sms_table", null, values) != -1;
    }

    /**
     * 删除短信
     * @param bean
     * @return
     */
    public boolean deleteSms(SmsBean bean){
        return db.delete(MainApplication.smsTableName, "id=?", new String[]{bean.getSmsID()})>0;
    }

    /**
     * 读取短信
     * @return
     */
    public List<SmsBean> readSms() {
        Cursor cursor = db.query(MainApplication.smsTableName, null, null, null, null, null, null);
        List<SmsBean> smsBeanList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SmsBean bean = new SmsBean();
                bean.setSmsID(cursor.getString(cursor.getColumnIndex("id")));
                bean.setSmsDate(cursor.getString(cursor.getColumnIndex("date")));
                bean.setSmsType(cursor.getString(cursor.getColumnIndex("type")));
                bean.setSmsBody(cursor.getString(cursor.getColumnIndex("body")));
                bean.setSmsAddress(cursor.getString(cursor.getColumnIndex("address")));
                bean.setSmsPerson(cursor.getString(cursor.getColumnIndex("person")));
                smsBeanList.add(bean);
            } while (cursor.moveToNext());
        }
        return smsBeanList;
    }

    /**
     * 记取通话记录
     * @return
     */
    public List<CallMsgBean> readCallMsg() {
        List<CallMsgBean> beans = new ArrayList<>();
        Cursor cursor = db.query(MainApplication.callMsgTableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CallMsgBean bean = new CallMsgBean();
                bean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                bean.setPerson(cursor.getString(cursor.getColumnIndex("person")));
                bean.setDate(FormatUtils.getStandardDate(cursor.getLong(cursor.getColumnIndex("date")),"yyyy-MM-dd HH:mm:ss"));
                bean.setType(cursor.getString(cursor.getColumnIndex("type")));
                bean.setCallMsgId(cursor.getString(cursor.getColumnIndex("id")));
                beans.add(bean);
            } while (cursor.moveToNext());
        }
        return beans;
    }

    /**
     * 保存通话记录到数据库
     * @param callMsgBean
     * @return
     */
    public boolean saveCallMsg(CallMsgBean callMsgBean) {
        values.clear();
        values.put("person", callMsgBean.getPerson());
        values.put("date", FormatUtils.getStandardDate(callMsgBean.getDate(),"yyyy-MM-dd HH:mm:ss").getTime());
        values.put("type", callMsgBean.getType());
        values.put("address", callMsgBean.getAddress());
        return db.insert(MainApplication.callMsgTableName, null, values) >0;
    }

    /**
     * 删除指定通话记录
     * @param bean
     * @return
     */
    public boolean deleteCallMsg(CallMsgBean bean){
        return db.delete(MainApplication.callMsgTableName,"id=?",new String[]{bean.getCallMsgId()})>0;
    }
}
