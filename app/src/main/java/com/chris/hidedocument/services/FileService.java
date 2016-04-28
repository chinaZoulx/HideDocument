package com.chris.hidedocument.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.chris.hidedocument.listener.OnSearchFileListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.tools.HideUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/23.
 */
public class FileService {

    private static FileService instance;
    private static List<FileInfoBean> mFileInfoBeanList;
    public static final int SEARCH_FINISH = 0x11;
    /**
     * 常用视频格式
     */
    public static String[] extensions=new String[]{".mp4",".3gp",".avi",".rmvb",".dat",".wmv",".amv",".dmv",".mtv",".rm"};

    public static FileService newInstance() {
        if (instance == null) {
            instance = new FileService();
            mFileInfoBeanList = new ArrayList<>();
        }
        return instance;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEARCH_FINISH:
                    ((OnSearchFileListener) msg.obj).onSearchFinish(mFileInfoBeanList);
                    break;
            }
        }
    };

    public void sendMessage(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    /**
     * 搜索目录，扩展名(判断的文件类型的后缀名)，是否进入子文件夹
     *
     * @param Path
     * @param Extension
     * @param IsIterative
     * @return
     */
    public List<FileInfoBean> getFiles(String Path, String Extension, boolean IsIterative) {
        List<FileInfoBean> fileList = new ArrayList<>();
        File[] files = new File(Path).listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.exists()) {
                if (f.isFile()) {
                    String fileName = f.getName();
                    String tempPostfix = "";
                    int startIndex = fileName.lastIndexOf(".");
                    if (startIndex != -1) {
                        tempPostfix = fileName.substring(startIndex, fileName.length());
                    }
                    Log.e("TEST", fileName + " 扩展名：" + tempPostfix);
                    // 判断扩展名
                    if (tempPostfix.equals(Extension)) {
                        FileInfoBean bean = new FileInfoBean();
                        bean.setFilePath(f.getPath());
                        String tempName = fileName.substring(0, startIndex);
                        if (tempName.lastIndexOf(".") != -1) {
                            bean.setFileType(tempName.substring(tempName.lastIndexOf("."), tempName.length()));
                        }
                        bean.setFileName(tempName.substring(0, startIndex));
                        fileList.add(bean);
                    }
                    if (!IsIterative)
                        break;  //如果不进入子集目录则跳出
                } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
                    getFiles(f.getPath(), Extension, IsIterative);  //这里就开始递归了
            }
        }
        return fileList;
    }


    /**
     * 异步的方式获取文件并回调监听
     * @param baseFile
     * @param extensions
     * @param onSearchFileListener
     */
    public synchronized void getFiles(final File baseFile, final String[] extensions, final OnSearchFileListener onSearchFileListener) {

        mFileInfoBeanList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchFiles(baseFile, extensions);
                sendMessage(SEARCH_FINISH, onSearchFileListener);
            }
        }).start();

    }

    /**
     * 搜索指定后缀名的文件
     * @param baseFile
     * @param extensions
     * @return
     */
    private List<FileInfoBean> searchFiles(final File baseFile, final String[] extensions) {

        baseFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                boolean flag = false;
                String name = file.getName();
                int index = name.lastIndexOf(".");
                if (index != -1) {
                    String tempExtension = name.substring(index);
                    //是否是指定的文件
                    for (String extension : extensions) {
                        if (tempExtension.equalsIgnoreCase(extension)) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        //保存到列表，以供显示
                        FileInfoBean bean = new FileInfoBean();
                        bean.setFileName(name);
                        bean.setFilePath(file.getAbsolutePath());
                        bean.setFileInfo((file.getTotalSpace() / 1024) + "kb");
                        //
                        String tempName = name.substring(0, index);//原文件名
                        int typeIndex = tempName.lastIndexOf(".");//最后一个点的位置
                        String type = tempExtension;
                        if (typeIndex != -1) {
                            type = tempName.substring(typeIndex);
                        }
                        bean.setFileType(type);
                        bean.setFileId(file.length() + "");
                        mFileInfoBeanList.add(bean);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    searchFiles(file, extensions);
                }
                return false;
            }
        });

        return mFileInfoBeanList;
    }


    /**
     * @description:通过contentprovider获得sd卡上的图片
     * @author:hui-ye
     * @return:void
     */
    public List<FileInfoBean> getImages(Context context) {
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
        return imageList;
    }

    /**
     * 获取视频
     * @param context
     * @return
     */
    public List<FileInfoBean> getVideos(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA
        };
        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " desc";
        Cursor mCursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOrder);
        List<FileInfoBean> fileInfoBeanList = new ArrayList<>();
        if (mCursor != null) {
            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                FileInfoBean bean = new FileInfoBean();
                bean.setFileInfo(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media._ID)));
                bean.setFileName(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                bean.setFilePath(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                bean.setFileInfo(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                  //获取视频封面图
//                bean.setFileColver(MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, Long.parseLong(bean.getFileId()), MediaStore.Video.Thumbnails.MICRO_KIND, null));

                fileInfoBeanList.add(bean);
            }

            mCursor.close();

        }
        return fileInfoBeanList;
    }

}
