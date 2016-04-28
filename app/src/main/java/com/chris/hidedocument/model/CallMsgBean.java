package com.chris.hidedocument.model;

/**
 * Created by zoulx on 2016/4/8.
 */
public class CallMsgBean {

    private String address;
    private String person;
    private String Date;
    private String type;
    private boolean isCheck;
    private String callMsgId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getCallMsgId() {
        return callMsgId;
    }

    public void setCallMsgId(String callMsgId) {
        this.callMsgId = callMsgId;
    }
}
