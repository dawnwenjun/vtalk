package com.talk.service;

import com.talk.bean.Messagebean;

//客户端连接服务器
public class userClient {
    public Messagebean sendLogin(Messagebean u){
        return  new connectServer().login(u);
    }

}
