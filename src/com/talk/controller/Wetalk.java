package com.talk.controller;

import com.server.utils.ManageClientThread;
import com.talk.service.connectServer;
import com.talk.bean.Messagebean;
import com.talk.service.userClient;
import com.talk.utils.ManageClientConServerThread;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.scene.text.TextAlignment.CENTER;

public class Wetalk implements Initializable{
//    Socket socket=connectServer.socket;
    @FXML
    private FlowPane wetalklist;
    @FXML
    private TextArea ChatList;
    @FXML
    private Label friendnamelabel;
    @FXML
    private TextArea talkcontext;

    @FXML
    void closeStage(MouseEvent event) {
        Stage wetalkStage = (Stage) talkcontext.getScene().getWindow();
        wetalkStage.close();
    }
    @FXML
    void minstage(MouseEvent event) {
        Stage wetalkStage = (Stage) talkcontext.getScene().getWindow();
        wetalkStage.setIconified(true);
    }


    public void setMessage(Messagebean messagebean){
        //获取发送者 姓名 头像 发送内容
                System.out.println("执行到我---------------");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HBox hBox = new HBox();

                        Map<String, Object> NumberItem=messagebean.getUserinfo();

                        ImageView imageView = new ImageView();
                        Image Numberimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+NumberItem.get("imgurl"));
                        imageView.setImage(Numberimage);
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);

                        Rectangle rectangle = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
                        rectangle.setArcHeight(imageView.prefHeight(-1));
                        rectangle.setArcWidth(imageView.prefWidth(-1));
                        imageView.setClip(rectangle);


                        Label NumberList=new Label((String)(messagebean.getReturnContext()));


                        NumberList.setWrapText(true);
                        NumberList.setMaxWidth(250);
                        NumberList.setPadding(new Insets(6));
                        NumberList.setFont(new Font(14));
                        String username=(String) NumberItem.get("uname");


                        hBox.setPrefWidth(550);
                        //如果是自己发的messageBox.setAlignment(Pos.TOP_RIGHT);
                        Map<String,Object> userInfo=Main.userInfo;
                        String uname=(String)userInfo.get("uname"); //当前用户
//                        hBox.setPadding(new Insets(10, , 10, 5));
                        hBox.setPadding(new Insets(5,0,10,0));

                        System.out.println(uname+"本地");
                        System.out.println(username+"从服务端获取");

                        double[] points;


                        if(username.equals(uname)){

                            points = new double[]{
                                    0.0, 0.0,
                                    0.0, 12.0,
                                    12.0, 6.0
                            };
                            NumberList.setStyle("-fx-background-color: rgb(38, 131, 245); -fx-background-radius: 7px;");
                            NumberList.setTextFill(Color.web("#ffffff"));

                            hBox.setMargin(NumberList, new Insets(15,0,5,0));

                            Polygon triangle = new Polygon(points);
                            hBox.setMargin(triangle, new Insets(21,0,5,0));


                            triangle.setFill(Color.rgb(38, 131, 245));

                            hBox.getChildren().addAll(NumberList,triangle,imageView);
                            hBox.setAlignment(Pos.TOP_RIGHT);
                            wetalklist.getChildren().addAll(hBox);
                        }else
                        {
                            points = new double[]{
                                    0.0, 6.0,
                                    12.0, 0.0,
                                    12.0, 12.0
                            };
                            Polygon triangle = new Polygon(points);
                            triangle.setFill(Color.rgb(226, 192, 192));
                            hBox.setMargin(triangle, new Insets(21,0,5,0));
                            hBox.setMargin(NumberList, new Insets(15,0,5,0));


                            NumberList.setStyle("-fx-background-color: rgb(226, 192, 192); -fx-background-radius: 7px;");
                            hBox.getChildren().addAll(imageView,triangle,NumberList);
                            wetalklist.getChildren().addAll(hBox);

                        }
                    }
                });


    }
    @FXML
    void sendMessage(MouseEvent event) {

        Map<String,Object> nowFriendInfo=Main.nowFriendInfo;
        Map<String,Object> userInfo=Main.userInfo;
        String uname=(String)userInfo.get("uname"); //当前用户
        String fname=(String)nowFriendInfo.get("uname"); //聊天朋友

        //当前用户正在和朋友聊天
        Messagebean msbean=new Messagebean();

        //设置发送者
        msbean.setSender(uname);
        //设置接收者
        msbean.setRecipient(fname);
        //设置发送时间
        msbean.setSendTime(new java.util.Date().toString());
       // 设置发送内容
        msbean.setReturnContext(talkcontext.getText());
        //设置发送信息类型
        msbean.setReturnType("2");

        //向服务器端发送

        Socket socket=ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();

        try {
            ObjectOutputStream   objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(msbean);

        } catch (IOException e) {
            e.printStackTrace();
        }
        talkcontext.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String,Object> nowFriendInfo=Main.nowFriendInfo;
        String fnickname=(String)nowFriendInfo.get("nickname"); //聊天朋友

        Map<String,Object> userInfo=Main.userInfo;
        String uname=(String)userInfo.get("uname"); //当前用户

        friendnamelabel.setText(fnickname);
        friendnamelabel.setFont(new Font(20));
    }


}

