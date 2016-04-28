package com.chris.hidedocument.tools;

import android.os.Handler;
import android.os.Message;

import com.chris.hidedocument.listener.OnHideListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.model.HideBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoulx on 2016/3/22.
 */
public class HideUtils {

    public static final String postfix = ".h45id1ePo3st3fi0x1";
    private static HideUtils instance;

    private List<HideBean> mHideBeanList;
    private List<FileInfoBean> mFileInfoBeans;
    private final static int FINISH=0;
    public int currentTab=0;

    public static HideUtils newInstance(){
        if(instance==null){
            instance=new HideUtils();
        }
        return instance;
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FINISH:
                    showData(mFileInfoBeans);
                    break;
            }
        }
    };

    private void showData(List<FileInfoBean> fileInfoBeanList){
        for(HideBean bean:mHideBeanList){
            if(bean.getId()==currentTab){
                bean.getOnHideListener().onHideFinish(fileInfoBeanList);
            }
        }
    }

    /**
     * 是否隐藏文件
     *
     * @param flag
     */
    public void isHideFile(final boolean flag, final List<FileInfoBean> fileInfoBeans) {
        this.mFileInfoBeans=fileInfoBeans;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag) {//隐藏
                    for (int i=0;i<mFileInfoBeans.size();i++) {
                        FileInfoBean bean=mFileInfoBeans.get(i);
                        if (bean.isCheck()) {
                                if (hide(bean.getFilePath())) {
                                    mFileInfoBeans.remove(bean);
                                    i--;
                                }
                        }
                    }
                }else {
                    for (int i=0;i<mFileInfoBeans.size();i++) {
                        FileInfoBean bean=mFileInfoBeans.get(i);
                        if (bean.isCheck()) {
                            if(show(bean.getFilePath())){
                                mFileInfoBeans.remove(bean);
                                i--;
                            }
                        }
                    }
                }
                sendMessage(FINISH);
            }
        }).start();
    }

    /**
     * 隐藏文件
     * @param filePath
     * @return
     */
    private boolean hide(String filePath) {
        boolean flag = false;

        File file = new File(filePath);
        if (file.exists()) {
            if (file.renameTo(new File(filePath + postfix))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 显示文件
     * @param filePath
     * @return
     */
    private boolean show(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        if (file.exists()) {
            String path = filePath.substring(0, filePath.lastIndexOf(postfix));
            if (file.renameTo(new File(path))) {
                flag = true;
            }
        }
        return flag;
    }

    private void sendMessage(int what){
        Message msg=new Message();
        msg.what=what;
        mHandler.sendMessage(msg);
    }

    public void setOnHideListener(OnHideListener onHideListener,int id){
        if(mHideBeanList==null){
            mHideBeanList=new ArrayList<>();
        }
        HideBean bean=new HideBean();
        bean.setOnHideListener(onHideListener);
        bean.setId(id);
        this.mHideBeanList.add(bean);
    }

    public void setCurrentTab(int currentTab){
        this.currentTab=currentTab;
    }



}
