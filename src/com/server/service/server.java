package com.server.service;

import com.server.dao.userDao;
import com.server.utils.ManageClientThread;
import com.talk.bean.Messagebean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class server {
    ServerSocket serverSocket=null;

    public void serverStart(){

                while(true){
                    try {
                        serverSocket = new ServerSocket(6666);
                    } catch (IOException e) {
                        System.out.println("正在监听");
                    }

                    try {
                        System.out.println("正在等待客户");
                        Socket acceptUser = serverSocket.accept();

                        System.out.println("客户的地址:"+acceptUser.getInetAddress());

                        ObjectInputStream objectInputStream = new ObjectInputStream(acceptUser.getInputStream());
                        Messagebean  mbean =(Messagebean)objectInputStream.readObject();

                        if(mbean.getReturnType().equals("1")) {

                            //获取客户端账号密码
                            String username = (String) mbean.getUserinfo().get("uname");
                            String upassword = (String) mbean.getUserinfo().get("upassword");

                            userDao userdao = new userDao();
                            Messagebean messagebean = new Messagebean();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(acceptUser.getOutputStream());
                            //数据库验证
                            if (userdao.login(username, upassword)) {
                                //输入正确
                                /**
                                 * 返回值
                                 * 1.个人信息
                                 * 2.好友列表
                                 * 3.群信息
                                 */
                                //1.个人信息
                                messagebean.setUserinfo(userdao.selectByname(username));
                                //2.好友列表
                                messagebean.setFriendsinfo(userdao.selectFriendByname(username));
                                //3.群信息
                                messagebean.setGroupsinfo(userdao.getGroupsinfoByname(username));

                                //将messagebean发送给客户端
                                objectOutputStream.writeObject(messagebean);


                                //新建一个线程 用来实现用户聊天
                                handler itemhandler = new handler(acceptUser);
                                itemhandler.start();
                                //将用户线程加入hashmap中
                                ManageClientThread.addClientThread(username,itemhandler);

                            } else {
                                //输入错误

                                //返回一个null  关闭连接
                                messagebean = null;
                                objectOutputStream.writeObject(messagebean);

                                //输入错误  关闭连接
                                acceptUser.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
    }

     public static void main(String[] args) {
        server vtalkserver = new server();
        vtalkserver.serverStart();
    }
}






