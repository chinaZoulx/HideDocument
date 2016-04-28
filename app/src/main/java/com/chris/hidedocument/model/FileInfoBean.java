package com.chris.hidedocument.model;

import android.graphics.Bitmap;

/**
 * Created by zoulx on 2016/3/21.
 */
public class FileInfoBean {
    private String fileId;//文件ID
    private String fileName;//文件名
    private String filePath;//文件路径
    private String fileType="未知";//文件类型
    private String fileInfo;//文件信息
    private Bitmap fileColver;//文件封面图
    private boolean isShow;//是否已经隐藏or显示
    private boolean isCheck;//是否已经选中

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Bitmap getFileColver() {
        return fileColver;
    }

    public void setFileColver(Bitmap fileColver) {
        this.fileColver = fileColver;
    }
}
