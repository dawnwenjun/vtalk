package com.talk.utils;

import com.talk.controller.Wetalk;
import javafx.scene.Scene;

import java.util.HashMap;

/**
 * 管理用户聊天界面的管理类
 */
public class MangeScene {
    public static HashMap SceneHashMap=new HashMap<String, Wetalk>();
    public static void addWetalk(String senderAndrecipient,Wetalk wetalk){
        SceneHashMap.put(senderAndrecipient,wetalk);
    }
    public static Wetalk getWetalk(String senderAndrecipient){
        return (Wetalk)SceneHashMap.get(senderAndrecipient);
    }
    public static Number getNumber(){
        return SceneHashMap.size();
    }

}
