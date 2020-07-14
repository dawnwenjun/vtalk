package com.talk.service;

import com.talk.bean.Messagebean;
import com.talk.utils.ClientConServerThread;
import com.talk.utils.ManageClientConServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//客户端向服务区端发送请求
public class connectServer {

    public  Socket socket;

    {
        try {
            socket = new Socket("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ;
    /**
     * 如果设置为静态  每次登录的都是同一个socket
     */
//    public static Socket socket;
//
//    {
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public Object obj;


    Messagebean login(Object obj){
        Messagebean msgbean=null;
        while(true){
            try {
                //发送
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(obj);

                //收到
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                msgbean=(Messagebean)objectInputStream.readObject();

                if(msgbean==null){


                    return null;
                }else
                 {
                    String uname=(String)msgbean.getUserinfo().get("uname");

                     //新开一个线程  专门用来管理与服务器进行通信
                     ClientConServerThread clientConServerThread = new ClientConServerThread(socket);
                     clientConServerThread.start();
                     ManageClientConServerThread.addClientConServerThread(uname,clientConServerThread);
                     System.out.println(ManageClientConServerThread.getNumber()+"线程数量");
                     return msgbean;
                 }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
