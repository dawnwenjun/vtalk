package com.server.utils;

import com.server.service.handler;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *  用来管理客户端线程
 */
public class ManageClientThread {
    public static HashMap clientThreadHashMap=new HashMap<String, handler>();
    public static void addClientThread(String uname,handler clientThreadHandler){

        //向clientThreadHashMap添加一个客户端通信线程
        clientThreadHashMap.put(uname,clientThreadHandler);
    }
    public static handler getClientThread(String uname){

        return (handler) clientThreadHashMap.get(uname);
    }
    public static int getonLogin(){
        return clientThreadHashMap.size();
    }

    //返回当前在线人数情况
    public static  List<String> getAllOnline(){
        Iterator it =clientThreadHashMap.keySet().iterator();
        List<String> onlinelist=new ArrayList<String>();
        while (it.hasNext()) {

            String key = (String) it.next();
            //将收到信息进行转发
            onlinelist.add(key);
        }
        return onlinelist;
    }
}
