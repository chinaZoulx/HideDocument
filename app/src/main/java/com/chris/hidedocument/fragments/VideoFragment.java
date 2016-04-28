package com.chris.hidedocument.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.ImageRecyclerAdapter;
import com.chris.hidedocument.adapter.VideoRecyclerAdapter;
import com.chris.hidedocument.listener.OnHideListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.listener.OnSearchFileListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.services.FileService;
import com.chris.hidedocument.services.ImageService;
import com.chris.hidedocument.tools.HideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class VideoFragment extends Fragment {
    public static final int ID=1;
    private RecyclerView mVideoRecyclerView;
    private VideoRecyclerAdapter mVideoRecyclerAdapter;
    private static List<FileInfoBean> mFileInfoBeanList;
    private TextView hint;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.tab_video,container,false);
        init();
        return mView;
    }

    private void init(){
        mFileInfoBeanList=new ArrayList<>();

        initView();
    }

    private void initView(){
        mVideoRecyclerView= (RecyclerView) mView.findViewById(R.id.videoRecyclerView);
        hint= (TextView) mView.findViewById(R.id.hint);
        mVideoRecyclerAdapter =new VideoRecyclerAdapter(mFileInfoBeanList);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        mVideoRecyclerView.setLayoutManager(manager);
        mVideoRecyclerView.setAdapter(mVideoRecyclerAdapter);
        mVideoRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                FileInfoBean bean = mFileInfoBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                mFileInfoBeanList.set(position, bean);
                mVideoRecyclerAdapter.setData(mFileInfoBeanList);
            }
        });
        mVideoRecyclerView.setVisibility(View.GONE);
        hint.setVisibility(View.VISIBLE);
        initData();
    }

    private void initData(){
        allScan();
        FileService.newInstance().getFiles(Environment.getExternalStorageDirectory(), FileService.extensions, new OnSearchFileListener() {
            @Override
            public void onSearchFinish(List<FileInfoBean> fileInfoBeanList) {
                mFileInfoBeanList=fileInfoBeanList;
                mVideoRecyclerView.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                mVideoRecyclerAdapter.setData(mFileInfoBeanList);
            }
        });

        HideUtils.newInstance().setOnHideListener(new OnHideListener() {
            @Override
            public void onHideFinish(List<FileInfoBean> fileInfoBeanList) {
                mVideoRecyclerAdapter.setData(fileInfoBeanList);
            }
        },ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        hint.setVisibility(View.VISIBLE);
        mVideoRecyclerView.setVisibility(View.GONE);
        initData();
    }

    public void allScan(){
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public static List<FileInfoBean> getData(int id){
        if(id==ID){
            return mFileInfoBeanList;
        }
        return null;
    }
}
