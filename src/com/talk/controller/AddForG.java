package com.talk.controller;

import com.talk.bean.Messagebean;
import com.talk.utils.ManageClientConServerThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AddForG implements Initializable {
    @FXML
    private Label addgroupabel;

    @FXML
    private TextField addnum;

    @FXML
    private Label addfriendlabel;

    public String clickbtn="friend";

    @FXML
    void closestage(MouseEvent event) {
        Stage addStage = (Stage) addnum.getScene().getWindow();
        addStage.close();
    }

    @FXML
    void minstage(MouseEvent event) {
        Stage addStage = (Stage) addnum.getScene().getWindow();
        addStage.setIconified(true);
    }

    @FXML
    void clickgroup(MouseEvent event) {
        clickbtn="group";
        addgroupabel.setTextFill(Color.web("#f35e5e"));
        addfriendlabel.setTextFill(Color.web("#806c6c"));
    }

    @FXML
    void clickfriend(MouseEvent event) {
        clickbtn="friend";
        addgroupabel.setTextFill(Color.web("#806c6c"));
        addfriendlabel.setTextFill(Color.web("#f35e5e"));
    }

    @FXML
    void sendForG(MouseEvent event) {
        /**
         * 点击按钮发送
         */
        Messagebean userAllInfo= Login.UserAllInfo;
        Map<String,Object> userinfo=userAllInfo.getUserinfo();
        String friendNum=addnum.getText();

        Map<String,Object> userInfo=Main.userInfo;
        String uname=(String)userInfo.get("uname");

        Messagebean msbean=new Messagebean();
        Messagebean msbean1=new Messagebean();
        //设置发送者
        msbean.setSender(uname);
        msbean1.setSender(uname);
        //向服务器端发送

        Socket socket= ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();
        if(clickbtn.equals("friend")){

            //设置发送信息类型
            msbean.setFriendNum(friendNum);
            msbean.setReturnType("6");

            msbean1.setFriendNum(friendNum);
            msbean1.setReturnType("12");

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(msbean);

                ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream1.writeObject(msbean1);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            msbean.setReturnType("7");
            msbean.setGrounpNum(friendNum);

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(msbean);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        addnum.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addfriendlabel.setTextFill(Color.web("#f35e5e"));
    }
}
