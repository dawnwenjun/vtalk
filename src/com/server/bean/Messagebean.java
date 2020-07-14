package com.server.bean;

public class Messagebean implements java.io.Serializable{
    private String returnType; //发送类型
    private String returnContext; //发送类型

    private  String sender;  //发送者
    private  String recipient;  //接受者
    private String sendTime;//发送时间

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendTime() {
        return sendTime;
    }
    public String getReturnType() {
        return returnType;
    }

    public String getReturnContext() {
        return returnContext;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setReturnContext(String returnContext) {
        this.returnContext = returnContext;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
