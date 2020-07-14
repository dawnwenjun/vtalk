package com.talk.utils;

import com.server.service.handler;
import com.server.utils.ManageClientThread;
import com.talk.controller.Grouptalk;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class ManageGroup {
    public static HashMap SceneHashMap=new HashMap<String, Grouptalk>();
    public static void addGrouptalk(String senderAndrecipient, Grouptalk grouptalk){
        /**
         * 学会  key  不允许重复
         */
        SceneHashMap.put(senderAndrecipient,grouptalk);

    }
    public static Grouptalk getGrouptalk(String senderAndrecipient){
        return (Grouptalk)SceneHashMap.get(senderAndrecipient);
    }
    public static int getNumber(){
        return SceneHashMap.size();
    }
}
