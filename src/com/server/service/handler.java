package com.server.service;

import com.Main;
import com.server.dao.userDao;
import com.server.utils.ManageClientThread;
import com.talk.bean.Messagebean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class handler extends Thread {

    Socket socket;

    public handler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            while (true) {
                //接受客户端发送的信息
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                    Messagebean mbean = (Messagebean) objectInputStream.readObject();
                    System.out.println("发送类型" + mbean.getReturnType());

                    /**
                     * 服务器端根据返回值来进行判断
                     *  returnType
                     */

                    if (mbean.getReturnType().equals("2")) {
                        /**
                         * 获取到的信息  sender Recipient SendTime ReturnContext
                         */
                        String Recipient = mbean.getRecipient();

                        String Sender = mbean.getSender();

                        userDao userdao = new userDao();
                        Map<String, Object> userInfo = userdao.selectByname(mbean.getSender());
                        mbean.setUserinfo(userInfo);

                        //将收到信息进行转发
                        handler RecipientclientThread = ManageClientThread.getClientThread(Recipient);

                        handler SenderclientThread = ManageClientThread.getClientThread(Sender);

                        if (RecipientclientThread != null) {
                            ObjectOutputStream RecipientobjectOutputStream = new ObjectOutputStream(RecipientclientThread.socket.getOutputStream());
                            RecipientobjectOutputStream.writeObject(mbean);
                        }

                        if (SenderclientThread != null) {
                            mbean.setReturnType("5");
                            ObjectOutputStream SenderobjectOutputStream = new ObjectOutputStream(SenderclientThread.socket.getOutputStream());
                            SenderobjectOutputStream.writeObject(mbean);
                        }

                    } else if (mbean.getReturnType().equals("3")) {

                        int groupindex = mbean.getGroupindex();
                        userDao userdao = new userDao();
                        ArrayList<Map<String, Object>> indexInfo = userdao.getGroupsnumberByindex(groupindex);
//                        System.out.println(indexInfo);

                        Messagebean messagebean = new Messagebean();
                        messagebean.setGroupItemNumbers(indexInfo);
                        messagebean.setReturnType("3");
                        messagebean.setSender(mbean.getSender());

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(messagebean);
                    } else if (mbean.getReturnType().equals("4")) {
                        //进行转发到别的群
                        // 获取键集合的迭代器

                        /**
                         * 获取到的信息  sender  SendTime ReturnContext
                         */

                        userDao userdao = new userDao();
                        Map<String, Object> userInfo = userdao.selectByname(mbean.getSender());
                        mbean.setUserinfo(userInfo);

                        System.out.println(mbean.getGroupindex());
                        Iterator it = ManageClientThread.clientThreadHashMap.keySet().iterator();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            System.out.println(key + "---------------key");
                            //将收到信息进行转发
                            handler RecipientclientThread = ManageClientThread.getClientThread(key);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(RecipientclientThread.socket.getOutputStream());
                            objectOutputStream.writeObject(mbean);
                        }

                    } else if (mbean.getReturnType().equals("6")) {
                        /**查询是否是自己是否是该用户好友
                         * no=>
                         *     查询是否有该用户
                         *     yes=>
                         *
                         *             yes=> 返回 字段
                         *             no=>  增加好友信息
                         *     no=> 返回null
                         */
                        String friendname = mbean.getFriendNum();
                        String username = mbean.getSender();
//                        System.out.println(username.equals(friendname)+"-------------------判断是不是自己");
                        userDao userdao = new userDao();
                        Map<String, Object> FienndMap = userdao.selectByname(friendname);
                        Map<String, Object> othermap = new HashMap<>();
                        List<String> Onlinefrilist= new ArrayList<String>();
                        if (username.equals(friendname)) {
                            System.out.println("加自己干嘛");
                            mbean.setReturnres("加自己干嘛");
                            mbean.setUserinfo(othermap);
                            mbean.setOnloadstr(Onlinefrilist);
                        } else {
                            if (FienndMap.size() == 0) {
                                mbean.setReturnres("该用户未注册");
                                mbean.setUserinfo(othermap);
                                mbean.setOnloadstr(Onlinefrilist);
                            } else {
                                if (userdao.checkIsFriends(username, friendname)) {
                                    //为真
                                    mbean.setReturnres("他/她已经是你好友了");
                                    mbean.setUserinfo(othermap);
                                    mbean.setOnloadstr(Onlinefrilist);
                                } else {
                                    //增加好友信息
                                    userdao.addfriend(username, friendname);
                                    mbean.setReturnres("我替他/她答应了");
                                    mbean.setUserinfo(FienndMap);
                                    //拿到我的上线好友
                                    Onlinefrilist = selectonloadfriend(mbean);
                                    System.out.println("我的上线好友");
                                    System.out.println(Onlinefrilist);
                                    mbean.setOnloadstr(Onlinefrilist);
                                }
                            }
                        }

                        handler SenderclientThread = ManageClientThread.getClientThread(mbean.getSender());
                        ObjectOutputStream SenderobjectOutputStream = new ObjectOutputStream(SenderclientThread.socket.getOutputStream());
                        SenderobjectOutputStream.writeObject(mbean);

                    } else if (mbean.getReturnType().equals("7")) {
                        /**
                         *     查询是否有该群
                         *     yes=>
                         *             yes=> 返回 字段
                         *             no=>  增加该群
                         *     no=> 返回null
                         */
                        String GrounpNum = mbean.getGrounpNum();
                        String username = mbean.getSender();
                        userDao userdao = new userDao();
                        Map<String, Object> othermap = new HashMap<>();
                        if (userdao.isGroup(GrounpNum).get("groupName") != null) {
                            //存在该群
                            if (userdao.clickGroup(username, GrounpNum)) {
                                //查看用户是否有该群
                                mbean.setReturnres("你已经是该群成员");
                                mbean.setNewaddgroupinfo(othermap);
                            } else {
                                String groupid = (String) userdao.isGroup(GrounpNum).get("groupid");

                                userdao.addgroup(username, groupid);
                                mbean.setReturnres("我有关系，你偷偷得进,没人发现");
                                mbean.setNewaddgroupinfo(userdao.isGroup(GrounpNum));
                            }

                        } else {
                            mbean.setReturnres("该群未创建");
                            mbean.setNewaddgroupinfo(othermap);
                        }
                        handler SenderclientThread = ManageClientThread.getClientThread(mbean.getSender());
                        ObjectOutputStream SenderobjectOutputStream = new ObjectOutputStream(SenderclientThread.socket.getOutputStream());
                        SenderobjectOutputStream.writeObject(mbean);
                    } else if (mbean.getReturnType().equals("8")) {
                        //修改头像
                        userDao userdao = new userDao();
                        String sender = mbean.getSender();
                        String imgurl = mbean.getImgurl();
                        userdao.updateUserImg(sender, imgurl);
                        mbean.setReturnres("您要的头像修改好了");

                        handler SenderclientThread = ManageClientThread.getClientThread(mbean.getSender());
                        ObjectOutputStream SenderobjectOutputStream = new ObjectOutputStream(SenderclientThread.socket.getOutputStream());
                        SenderobjectOutputStream.writeObject(mbean);

                    } else if (mbean.getReturnType().equals("9")) {
                        //查询好友上线人数  获取上线人数 和好友人数 匹配相同的

                        List<String> Onlinefrilist = selectonloadfriend(mbean);

                        String sender = mbean.getSender();
                        mbean.setOnloadstr(Onlinefrilist);
                        handler SenderclientThread = ManageClientThread.getClientThread(mbean.getSender());
                        ObjectOutputStream SenderobjectOutputStream = new ObjectOutputStream(SenderclientThread.socket.getOutputStream());
                        SenderobjectOutputStream.writeObject(mbean);

                    } else if (mbean.getReturnType().equals("10")) {

                        List<String> allOnline = ManageClientThread.getAllOnline();
                        userDao userdao = new userDao();
                        List<String> Onlinefrilist = selectonloadfriend(mbean);

                        /**
                         * 遍历我的好友线程  拿到之后 发送所有好友信息
                         */

                        //取得好友的所有上线好友

                        for (int i = 0; i < Onlinefrilist.size(); i++) {
                            List<String> myfriendfriend = userdao.selectfriendname(Onlinefrilist.get(i)); //好友的好友
                            List<String> myOnlinefrilist = new ArrayList<String>();

                            for (int k = 0; k < myfriendfriend.size(); k++) {
                                for (int j = 0; j < allOnline.size(); j++) {
                                    if (allOnline.get(j).equals(myfriendfriend.get(k))) {
                                        //放入一个新的数组
                                        myOnlinefrilist.add(allOnline.get(j));
                                    }
                                }

                            }

                            //好友的好友的上线情况
                            mbean.setOnloadstr(myOnlinefrilist);

                            mbean.setRecipient(Onlinefrilist.get(i));
                            handler SenderclientThread1 = ManageClientThread.getClientThread(Onlinefrilist.get(i));
                            ObjectOutputStream SenderobjectOutputStream1 = new ObjectOutputStream(SenderclientThread1.socket.getOutputStream());
                            SenderobjectOutputStream1.writeObject(mbean);
                        }
                    } else if (mbean.getReturnType().equals("11")) {
                        /**
                         * 遍历我的好友线程  拿到之后 发送所有好友信息
                         */
                        userDao userdao = new userDao();
                        List<String> allOnline = ManageClientThread.getAllOnline();
                        List<String> Onlinefrilist = selectonloadfriend(mbean);
                        //取得好友的所有上线好友
                        for (int i = 0; i < Onlinefrilist.size(); i++) {
                            List<String> myfriendfriend = userdao.selectfriendname(Onlinefrilist.get(i)); //好友的好友
                            List<String> myOnlinefrilist = new ArrayList<String>();

                            for (int k = 0; k < myfriendfriend.size(); k++) {
                                for (int j = 0; j < allOnline.size(); j++) {
                                    if (allOnline.get(j).equals(myfriendfriend.get(k))) {
                                        //放入一个新的数组
                                        myOnlinefrilist.add(allOnline.get(j));
                                    }
                                }
                            }
                            mbean.setFriendsinfo(userdao.selectFriendByname(Onlinefrilist.get(i)));
                            mbean.setOnloadstr(myOnlinefrilist);
                            mbean.setRecipient(Onlinefrilist.get(i));
                            handler SenderclientThread1 = ManageClientThread.getClientThread(Onlinefrilist.get(i));
                            ObjectOutputStream SenderobjectOutputStream1 = new ObjectOutputStream(SenderclientThread1.socket.getOutputStream());
                            SenderobjectOutputStream1.writeObject(mbean);
                        }
                    }else if (mbean.getReturnType().equals("12")) {
                        /**
                         * 遍历我的好友线程  拿到之后 发送所有好友信息  好友的好友上线信息
                         */
                        Messagebean messagebean=new Messagebean();
                        messagebean.setSender((String)mbean.getFriendNum());
                        userDao userdao = new userDao();
                        List<String> Onlinefrilist = selectonloadfriend(messagebean);

                        String username = mbean.getSender();
                        Map<String, Object> FienndMap = userdao.selectByname(username);
                        /**
                         * 遍历我的好友线程  拿到之后 发送所有好友信息
                         */
                            //好友的好友的上线情况
                            mbean.setOnloadstr(Onlinefrilist);
                            mbean.setUserinfo(FienndMap);
                            mbean.setRecipient((String)mbean.getFriendNum());
                            handler SenderclientThread1 = ManageClientThread.getClientThread((String)mbean.getFriendNum());
                            if(SenderclientThread1!=null){
                                ObjectOutputStream SenderobjectOutputStream1 = new ObjectOutputStream(SenderclientThread1.socket.getOutputStream());
                                SenderobjectOutputStream1.writeObject(mbean);
                            }

                    }


            }

        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
//            e.printStackTrace();
            System.out.println("有好友下线");
        }
    }

    //查找我的上线好友
    public List<String> selectonloadfriend(Messagebean mbean) {
        userDao userdao = new userDao();
        List<String> myfriend = userdao.selectfriendname(mbean.getSender()); //我的好友
        List<String> allOnline = ManageClientThread.getAllOnline();

        //查找我的上线好友
        List<String> Onlinefrilist = new ArrayList<String>();
        for (int i = 0; i < myfriend.size(); i++) {
            for (int j = 0; j < allOnline.size(); j++) {
                if (allOnline.get(j).equals(myfriend.get(i))) {
                    //放入一个新的数组
                    System.out.println(allOnline.get(j));
                    Onlinefrilist.add(allOnline.get(j));
                }
            }
        }
        return Onlinefrilist;
    }

}
