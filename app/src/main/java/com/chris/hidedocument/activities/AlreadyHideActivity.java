package com.chris.hidedocument.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.VideoRecyclerAdapter;
import com.chris.hidedocument.listener.OnHideListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.listener.OnSearchFileListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.services.FileService;
import com.chris.hidedocument.tools.HideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/23.
 */
public class AlreadyHideActivity extends AppCompatActivity {

    private static final int FINISH = 0;
    private static final int ID=0x4;
    private RecyclerView mAlreadyHideContentRecyclerView;
    private VideoRecyclerAdapter mAlreadyHideRecyclerAdapter;
    private List<FileInfoBean> mFileInfoBeanList;
//    private Toolbar mToolbar;
    private TextView hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_hide);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_msg,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.restore:
                Snackbar.make(mAlreadyHideContentRecyclerView,getString(R.string.is_sure),Snackbar.LENGTH_SHORT).setAction(getString(R.string.sure), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideUtils.newInstance().isHideFile(false, mFileInfoBeanList);
                    }
                }).setActionTextColor(Color.GRAY).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mFileInfoBeanList = new ArrayList<>();
        HideUtils.newInstance().setOnHideListener(new OnHideListener() {
            @Override
            public void onHideFinish(List<FileInfoBean> fileInfoBeanList) {
                mAlreadyHideRecyclerAdapter.setData(fileInfoBeanList);
            }
        },ID);
        initView();
    }

    private void initView() {
        hint= (TextView) findViewById(R.id.hint);
//        mToolbar= (Toolbar) findViewById(R.id.alreadyHideToolbar);
//        setSupportActionBar(mToolbar);
//        mToolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
//        mToolbar.setTitle(getString(R.string.already_hide));
//        mToolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v,getString(R.string.is_sure),Snackbar.LENGTH_SHORT).setAction(getString(R.string.sure), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        HideUtils.newInstance().isHideFile(false, mFileInfoBeanList);
//                    }
//                }).setActionTextColor(Color.GRAY).show();
//            }
//        });

        mAlreadyHideContentRecyclerView = (RecyclerView) findViewById(R.id.aleardyHideContentRecyclerView);
        //垂直显示一列
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
        mAlreadyHideContentRecyclerView.setLayoutManager(layoutManager);
        mAlreadyHideRecyclerAdapter = new VideoRecyclerAdapter(mFileInfoBeanList);
        mAlreadyHideContentRecyclerView.setAdapter(mAlreadyHideRecyclerAdapter);
        mAlreadyHideRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                FileInfoBean bean = mFileInfoBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                mFileInfoBeanList.set(position, bean);
                mAlreadyHideRecyclerAdapter.setData(mFileInfoBeanList);
            }
        });

        initData();
    }

    private void initData() {
        allScan();
        FileService.newInstance().getFiles(Environment.getExternalStorageDirectory(), new String[]{HideUtils.postfix}, new OnSearchFileListener() {
            @Override
            public void onSearchFinish(List<FileInfoBean> fileInfoBeanList) {
                mFileInfoBeanList=fileInfoBeanList;
                mAlreadyHideRecyclerAdapter.setData(fileInfoBeanList);
                hint.setVisibility(View.GONE);
                mAlreadyHideContentRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 标记全盘扫描，加快扫描速度
     */
    public void allScan(){
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideUtils.newInstance().setCurrentTab(ID);
    }
}
