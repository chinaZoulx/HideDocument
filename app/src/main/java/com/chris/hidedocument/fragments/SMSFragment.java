package com.chris.hidedocument.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.SmsRecyclerAdapter;
import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.listener.OnHideSmsListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.services.DBService;
import com.chris.hidedocument.services.SmsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class SMSFragment extends Fragment {

    private static int ID=2;
    private View mView;
    private RecyclerView mRecyclerView;
    private SmsRecyclerAdapter mSmsRecyclerAdapter;
    private static List<SmsBean> smsBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.tab_sms,container,false);
        init();
        return mView;
    }

    private void init(){
        smsBeanList=new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        smsBeanList= SmsService.newInstance().readSms(getActivity());
        mSmsRecyclerAdapter.setData(smsBeanList);
        SmsService.newInstance().setOnHideSmsListener(new OnHideSmsListener() {
            @Override
            public void onHideSmsFinish(List<SmsBean> smsBeanList) {
                mSmsRecyclerAdapter.setData(smsBeanList);
            }
        });
    }

    private void initView(){
        mRecyclerView= (RecyclerView) mView.findViewById(R.id.smsRecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mSmsRecyclerAdapter=new SmsRecyclerAdapter(smsBeanList);
        mRecyclerView.setAdapter(mSmsRecyclerAdapter);
        mSmsRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                SmsBean bean = smsBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                smsBeanList.set(position, bean);
                mSmsRecyclerAdapter.setData(smsBeanList);
            }
        });
    }

    public static List<SmsBean> getData(int id){
        if(id==ID){
            return smsBeanList;
        }
        return null;
    }
}
