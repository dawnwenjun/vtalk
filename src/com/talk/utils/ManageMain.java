package com.talk.utils;

import com.talk.controller.Main;
import com.talk.controller.Wetalk;

import java.util.HashMap;

public class ManageMain {
    public static HashMap SceneHashMap=new HashMap<String, Main>();
    public static void addMain(String senderAndrecipient, Main main){
        SceneHashMap.put(senderAndrecipient,main);
    }
    public static Main getMain(String senderAndrecipient){
        return (Main)SceneHashMap.get(senderAndrecipient);
    }
    public static Number getNumber(){
        return SceneHashMap.size();
    }
}
