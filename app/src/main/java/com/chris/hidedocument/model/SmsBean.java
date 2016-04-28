package com.chris.hidedocument.model;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SmsBean {
    private String smsID;
    private String smsThreadID;
    private String smsPerson;
    private String smsAddress;
    private String smsBody;
    private String smsDate;
    private String smsType;

    private boolean isCheck;

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getSmsPerson() {
        return smsPerson;
    }

    public void setSmsPerson(String smsPerson) {
        this.smsPerson = smsPerson;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getSmsID() {
        return smsID;
    }

    public void setSmsID(String smsID) {
        this.smsID = smsID;
    }

    public String getSmsThreadID() {
        return smsThreadID;
    }

    public void setSmsThreadID(String smsThreadID) {
        this.smsThreadID = smsThreadID;
    }
}
