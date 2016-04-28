package com.chris.hidedocument.fragments;

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
import com.chris.hidedocument.adapter.CallMsgAdapter;
import com.chris.hidedocument.listener.OnHideSmsListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.CallMsgBean;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.services.CallMsgService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class CallMsgFragment extends Fragment {

    private static final int ID=3;
    private View mView;
    private RecyclerView callMsgRecyclerView;
    private CallMsgAdapter callMsgAdapter;
    private static List<CallMsgBean> callMsgBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.tab_call_msg,container,false);
        init();
        initView();
        initData();
        return mView;
    }

    private void init(){
        callMsgBeanList=new ArrayList<>();
        callMsgAdapter=new CallMsgAdapter(callMsgBeanList);
    }

    private void initView(){
        callMsgRecyclerView= (RecyclerView) mView.findViewById(R.id.callMsgRecyclerView);
        callMsgRecyclerView.setAdapter(callMsgAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        callMsgRecyclerView.setLayoutManager(linearLayoutManager);
        callMsgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                CallMsgBean bean=callMsgBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                callMsgBeanList.set(position,bean);
                callMsgAdapter.setData(callMsgBeanList);
            }
        });
    }

    private void initData(){
        callMsgBeanList=CallMsgService.newInstance().readCallMsg(getActivity());
        callMsgAdapter.setData(callMsgBeanList);
        CallMsgService.newInstance().setOnHideSmsListener(new OnHideSmsListener() {
            @Override
            public void onHideCallMsgFinish(List<CallMsgBean> callMsgBeanList) {
                super.onHideCallMsgFinish(callMsgBeanList);
                callMsgAdapter.setData(callMsgBeanList);
            }
        });
    }

    public static List<CallMsgBean> getData(int id){
        if(id==ID){
            return callMsgBeanList;
        }
        return null;
    }
}
