package com.chris.hidedocument.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.CallMsgAdapter;
import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.frame.MyDatabaseHelper;
import com.chris.hidedocument.listener.OnHideSmsListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.CallMsgBean;
import com.chris.hidedocument.services.CallMsgService;
import com.chris.hidedocument.services.DBService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/4/9.
 */
public class AlreadyHideCallMsgActivity extends AppCompatActivity {

    private RecyclerView callMsgRecyclerView;
    private CallMsgAdapter callMsgAdapter;
    private List<CallMsgBean> callMsgBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_call_msg);
        init();
        initView();
        initData();
    }

    private void initView() {
        callMsgAdapter = new CallMsgAdapter(callMsgBeanList);
        callMsgRecyclerView = (RecyclerView) findViewById(R.id.callMsgRecyclerView);
        callMsgRecyclerView.setAdapter(callMsgAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        callMsgRecyclerView.setLayoutManager(linearLayoutManager);
        callMsgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                CallMsgBean bean = callMsgBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                callMsgBeanList.set(position, bean);
                callMsgAdapter.setData(callMsgBeanList);
            }
        });
    }

    private void initData() {
        callMsgBeanList = DBService.newInstance().readCallMsg();
        callMsgAdapter.setData(callMsgBeanList);
        CallMsgService.newInstance().setOnHideSmsListener(new OnHideSmsListener() {
            @Override
            public void onHideCallMsgFinish(List<CallMsgBean> callMsgBeanList) {
                super.onHideCallMsgFinish(callMsgBeanList);
                callMsgAdapter.setData(callMsgBeanList);
            }
        });
    }

    /**
     * 设置菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_msg, menu);
        return true;
    }

    /**
     * 菜单项被选中
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore:
                Snackbar.make(callMsgRecyclerView, "你确定要操作吗", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restore();
                    }
                }).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void init() {
        callMsgBeanList = new ArrayList<>();
    }

    /**
     * 恢复
     */
    private void restore() {

        for (int i = 0; i < callMsgBeanList.size(); i++) {
            CallMsgBean bean = callMsgBeanList.get(i);
            if (bean.isCheck()) {
                if (CallMsgService.newInstance().restoreCallMsg(this, bean)&&DBService.newInstance().deleteCallMsg(bean)) {
                        callMsgBeanList.remove(bean);
                        i--;
                }
            }
        }

        callMsgAdapter.setData(callMsgBeanList);
    }
}
