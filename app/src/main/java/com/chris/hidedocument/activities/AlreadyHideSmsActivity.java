package com.chris.hidedocument.activities;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.SmsRecyclerAdapter;
import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.services.DBService;
import com.chris.hidedocument.services.SmsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/4/7.
 */
public class AlreadyHideSmsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SmsRecyclerAdapter mSmsRecyclerAdapter;
    private List<SmsBean> mSmsBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_hide_sms);
        init();
        initView();
        initData();
    }

    private void init() {
        mSmsBeanList = new ArrayList<>();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.smsRecyclerView);
        mSmsRecyclerAdapter = new SmsRecyclerAdapter(mSmsBeanList);
        mRecyclerView.setAdapter(mSmsRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSmsRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                SmsBean bean = mSmsBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                mSmsRecyclerAdapter.setData(mSmsBeanList);
            }
        });
    }

    private void initData() {
        mSmsBeanList = DBService.newInstance().readSms();
        mSmsRecyclerAdapter.setData(mSmsBeanList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_msg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore:
                Snackbar.make(mRecyclerView, "你确定要操作吗", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restore();
                    }
                }).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restore() {
        for (int i = 0; i < mSmsBeanList.size(); i++) {
            SmsBean bean = mSmsBeanList.get(i);
            if (bean.isCheck()) {
                if (SmsService.newInstance().writeSms(this, bean) && DBService.newInstance().deleteSms(bean)) {
                    mSmsBeanList.remove(bean);
                    i--;
                }
            }
        }
        mSmsRecyclerAdapter.setData(mSmsBeanList);
    }
}
