package com.talk.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Messagebean implements java.io.Serializable{
    private String returnType; //发送类型
    private String returnContext; //发送类型

    private  String sender;  //发送者
    private  String recipient;  //接受者
    private  String sendTime;//发送时间

    private String friendNum;//申请加好友号
    private String returnres; //返回结果

    private String grounpNum; //申请加群号

    private Map<String, Object> userinfo; //个人信息
    private ArrayList<Map<String, Object>>  friendsinfo;  //好友信息
    private ArrayList<Map<String, Object>>   Groupsinfo;   //群信息
    private int groupindex;  //群序号

    private ArrayList<Map<String, Object>> groupItemNumbers;  //群索引全部信息
    private List<String> onloadstr;

    public  List<String>  getOnloadstr() {
        return onloadstr;
    }

    public void setOnloadstr( List<String>  onloadstr) {
        this.onloadstr = onloadstr;
    }

    public  String imgurl;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private Map<String, Object> newaddgroupinfo;

    public Map<String, Object> getNewaddgroupinfo() {
        return newaddgroupinfo;
    }

    public void setNewaddgroupinfo(Map<String, Object> newaddgroupinfo) {
        this.newaddgroupinfo = newaddgroupinfo;
    }

    public String getGrounpNum() {
        return grounpNum;
    }

    public void setGrounpNum(String grounpNum) {
        this.grounpNum = grounpNum;
    }

    public String getReturnres() {
        return returnres;
    }

    public void setReturnres(String returnres) {
        this.returnres = returnres;
    }

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }



    public ArrayList<Map<String, Object>> getGroupItemNumbers() {
        return groupItemNumbers;
    }

    public void setGroupItemNumbers(ArrayList<Map<String, Object>> groupItemNumbers) {
        this.groupItemNumbers = groupItemNumbers;
    }

    public int getGroupindex() {
        return groupindex;
    }

    public void setGroupindex(int groupindex) {
        this.groupindex = groupindex;
    }

    public ArrayList<Map<String, Object>> getGroupsinfo() {
        return Groupsinfo;
    }

    public void setGroupsinfo(ArrayList<Map<String, Object>> groupsinfo) {
        Groupsinfo = groupsinfo;
    }

    public ArrayList<Map<String, Object>> getFriendsinfo() {
        return friendsinfo;
    }

    public void setFriendsinfo(ArrayList<Map<String, Object>> friendsinfo) {
        this.friendsinfo = friendsinfo;
    }

    public Map<String, Object> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Map<String, Object> userinfo) {
        this.userinfo = userinfo;
    }


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
