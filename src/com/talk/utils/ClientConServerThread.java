package com.talk.utils;

import com.server.service.handler;
import com.server.utils.ManageClientThread;
import com.talk.bean.Messagebean;
import com.talk.controller.Main;
import com.talk.controller.Wetalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

/**
 * 客户端和服务器端保持通讯线程
 */
public class ClientConServerThread extends Thread{
    private Socket socket;

    public ClientConServerThread(Socket s){
        this.socket=s;
    }


    public Socket getSocket() {
        return socket;
    }


    public void run(){
        while (true){
            //不停得读取从服务端发来的消息
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Messagebean msbean= (Messagebean)objectInputStream.readObject();
                System.out.println(msbean.getReturnType());
                if(msbean.getReturnType().equals("2")){

                    //获取页面
                    Wetalk wetalk = MangeScene.getWetalk(msbean.getRecipient() + " " +msbean.getSender() );
                    if(wetalk!=null){
                        wetalk.setMessage(msbean);
                    }
                }else if(msbean.getReturnType().equals("3")){
//                    System.out.println(msbean.getGroupItemNumbers());
                    //获取页面
                    ManageMain.getMain(msbean.getSender()).getItemNumber(msbean.getGroupItemNumbers());
                }else if(msbean.getReturnType().equals("4")){
                    //群聊
                    //获取页面
//                    ManageMain.getWetalk(msbean.getSender()).getItemNumber(msbean.getGroupItemNumbers());
                    System.out.println("我收到了");
                    if(ManageGroup.getGrouptalk(msbean.getRecipient()+" ")!=null){
                        ManageGroup.getGrouptalk(msbean.getRecipient()+" ").setMessage(msbean);
                    }



                }else if(msbean.getReturnType().equals("5")){
                    Wetalk wetalkself = MangeScene.getWetalk(msbean.getSender() + " " +msbean.getRecipient() );
                    if(wetalkself!=null){
                        wetalkself.setMessage(msbean);
                    }
                }else if(msbean.getReturnType().equals("6")){

                    /**
                     *    拿到自己main页面
                     *    调用一个方法 用来弹窗以及  是否增加好友列表
                     */

                    String uname= msbean.getSender();
                    List<String> online=msbean.getOnloadstr();

                    System.out.println("调用一个方法 用来弹窗以及  是否增加好友列表");
                    System.out.println(online);


                   if(ManageMain.getMain(uname)!=null){
                       ManageMain.getMain(uname).IsAddfriend(msbean);
                       /*第二个函数早于第一个函数执行*/
                       try {
                           Thread.sleep (400) ;
                       } catch (InterruptedException ie){
                       }

                       if(online.size()!=0){
                           ManageMain.getMain(uname).updateNewfriend(online);
                       }
                   }
                }else if(msbean.getReturnType().equals("7")){

                    String uname= msbean.getSender();
                    if(ManageMain.getMain(uname)!=null){
                        ManageMain.getMain(uname).IsAddGroup(msbean);
                    }
                }else if(msbean.getReturnType().equals("8")){
                    String uname= msbean.getSender();

                   if(ManageMain.getMain(uname)!=null){
                        ManageMain.getMain(uname).updateimg(msbean);
                    }


                }else if(msbean.getReturnType().equals("9")){
                    List<String> online=msbean.getOnloadstr();
                    String uname= msbean.getSender();
                    if(ManageMain.getMain(uname)!=null){
                        ManageMain.getMain(uname).updateOnLoad(online);
                    }
                }else if(msbean.getReturnType().equals("10")){
                    List<String> online=msbean.getOnloadstr();
                    String uname= msbean.getRecipient();

                    if(ManageMain.getMain(uname)!=null){
                        ManageMain.getMain(uname).alertonline(online);
                    }
                }else if(msbean.getReturnType().equals("11")){
                    String Recipientuname= msbean.getRecipient();

                    if(ManageMain.getMain(Recipientuname)!=null){
                        ManageMain.getMain(Recipientuname).updateallimg(msbean);
                    }

                }else if(msbean.getReturnType().equals("12")){
                    String Recipientuname= msbean.getRecipient();
                    System.out.println(Recipientuname+"--------发给我的好友");

                    List<String> online=msbean.getOnloadstr();
                    System.out.println(online+"--------好友的好友上线情况");

                    if(ManageMain.getMain(Recipientuname)!=null){
                        ManageMain.getMain(Recipientuname).friendAddfriend(msbean);
                        /*第二个函数早于第一个函数执行*/
                        try {
                            Thread.sleep (400) ;
                        } catch (InterruptedException ie){
                        }

                        if(online.size()!=0){
                            ManageMain.getMain(Recipientuname).updateNewfriend(online);
                        }
                    }


                }



            }catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
         }
        }
    }
