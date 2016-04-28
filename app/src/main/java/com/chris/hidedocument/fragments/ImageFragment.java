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
import com.chris.hidedocument.listener.OnHideListener;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.listener.OnSearchFileListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.services.ImageService;
import com.chris.hidedocument.tools.HideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class ImageFragment extends Fragment {

    public static final int ID=0;
    private RecyclerView mainRecyclerView;
    private ImageRecyclerAdapter mImageRecyclerAdapter;
    private static List<FileInfoBean> mFileInfoBeanList;
    private ImageService mImageService;
    private View mView;
    private TextView hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.tab_images,container,false);
        init();
        return mView;
    }

    private void init(){
        mImageService=new ImageService(getActivity());
        mFileInfoBeanList=new ArrayList<>();

        initView();
    }

    private void initView(){
        hint= (TextView) mView.findViewById(R.id.hint);
        mainRecyclerView= (RecyclerView) mView.findViewById(R.id.imageRecyclerView);
        mImageRecyclerAdapter =new ImageRecyclerAdapter(mFileInfoBeanList);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        mainRecyclerView.setLayoutManager(manager);
        mainRecyclerView.setAdapter(mImageRecyclerAdapter);
        mImageRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View currentView, int position) {
                FileInfoBean bean = mFileInfoBeanList.get(position);
                bean.setIsCheck(!bean.isCheck());
                mFileInfoBeanList.set(position, bean);
                mImageRecyclerAdapter.setData(mFileInfoBeanList);
            }
        });
        hint.setVisibility(View.VISIBLE);
        mainRecyclerView.setVisibility(View.GONE);
        initData();
    }

    private void initData(){
        allScan();
        mImageService.getImages(new OnSearchFileListener() {
            @Override
            public void onSearchFinish(List<FileInfoBean> fileInfoBeanList) {
                mFileInfoBeanList=fileInfoBeanList;
                mImageRecyclerAdapter.setData(mFileInfoBeanList);
                hint.setVisibility(View.GONE);
                mainRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        HideUtils.newInstance().setOnHideListener(new OnHideListener() {
            @Override
            public void onHideFinish(List<FileInfoBean> fileInfoBeanList) {
                mImageRecyclerAdapter.setData(fileInfoBeanList);
            }
        },ID);
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
