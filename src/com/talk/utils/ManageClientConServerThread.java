package com.talk.utils;

import java.util.HashMap;

/**
 * 管理客户端和服务器端保持通信的线程类
 */
public class ManageClientConServerThread {
  public static HashMap ThreadHashMap=new HashMap<String,ClientConServerThread>();
    //把创建好的通讯线程放入hashmap
    public static void addClientConServerThread(String uname,ClientConServerThread clientConServerThread ){
        ThreadHashMap.put(uname,clientConServerThread);
    }

    public static ClientConServerThread getClientConServerThreadByuname(String uname){
        return (ClientConServerThread)ThreadHashMap.get(uname);
    }

    public static Number getNumber(){
        return ThreadHashMap.size();
    }
}
