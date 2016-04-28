package com.chris.hidedocument.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.chris.hidedocument.listener.OnSearchFileListener;
import com.chris.hidedocument.model.FileInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class ImageService {

    private Context context;

    public ImageService(Context context) {
        this.context = context;
    }

    /**
     * @description:通过contentprovider获得sd卡上的图片
     * @author:hui-ye
     * @return:void
     */
    public List<FileInfoBean> getImages(OnSearchFileListener onSearchFileListener) {
        // 指定要查询的uri资源
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 获取ContentResolver
        ContentResolver contentResolver = context.getContentResolver();
        // 查询的字段
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        // 条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
        // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
        String[] selectionArgs = {"image/jpeg"};
        // 排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        // 查询sd卡上的图片
        Cursor cursor = contentResolver.query(uri, projection, selection,
                selectionArgs, sortOrder);
        List<FileInfoBean> imageList = new ArrayList<FileInfoBean>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                FileInfoBean bean = new FileInfoBean();
                // 获得图片的id
                bean.setFileId(cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media._ID)));
                // 获得图片显示的名称
                bean.setFileName(cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                // 获得图片的信息
                bean.setFileInfo(""
                        + cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Images.Media.SIZE) / 1024)
                        + "kb");
                // 获得图片所在的路径(可以使用路径构建URI)
                bean.setFilePath(cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA)));
                imageList.add(bean);
            }
            // 关闭cursor
            cursor.close();
        }
        onSearchFileListener.onSearchFinish(imageList);
        return imageList;
    }
}
