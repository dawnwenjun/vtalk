package com.talk.controller;
import com.talk.bean.Messagebean;
import com.talk.service.userClient;
import com.talk.utils.ManageClientConServerThread;
import com.talk.utils.ManageMain;
import com.talk.utils.StageMove;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private Button loginbtn;

    @FXML
    private TextField username;
    @FXML
    private PasswordField userpw;

    @FXML
    private ImageView loginbac;

    @FXML
    private Label registerlab;
    @FXML
    private ImageView closeimg;

    @FXML
    void closestage(MouseEvent event) {
        Stage loginStage = (Stage) closeimg.getScene().getWindow();
        loginStage.close();
    }
    @FXML
    void minStage(MouseEvent event) {
        Stage loginStage = (Stage) closeimg.getScene().getWindow();
        loginStage.setIconified(true);
    }

    @FXML
    void onRegister(MouseEvent event) throws IOException {
        //跳转页面
        //先关闭当前场景,再开启你需要的场景
        Parent root=FXMLLoader.load(getClass().getResource("/com/talk/view/register.fxml"));
        Stage stage=(Stage)registerlab.getScene().getWindow();
        Scene scene=new Scene((root));
        stage.setScene(scene);
        new StageMove(stage,scene.getRoot());
    }

    public static  Messagebean  UserAllInfo;


    @FXML
    void onLogin(MouseEvent event) throws IOException {
        //获取账号密码
        String uname= username.getText();

        String upassword=userpw.getText();

        if(uname.length() == 0||upassword.length()==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入账号或者密码");
            alert.showAndWait();
        }else
        {
            //客户端连接服务器
            userClient userClient = new userClient();
            /**
             * 发送类型为用户信息  1
             */
            Messagebean messagebean = new Messagebean();

            Map<String, Object> userinfo= new HashMap<>();
            userinfo.put("uname",uname);
            userinfo.put("upassword",upassword);


            messagebean.setReturnType("1");
            messagebean.setUserinfo(userinfo);


            if( userClient.sendLogin(messagebean)!=null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("登录成功");
                alert.showAndWait();
                //将获取的信息作为全局变量 在Main界面能够接收到
                UserAllInfo=userClient.sendLogin(messagebean);

                Parent root= null;
                FXMLLoader Mainloader=null;
                try {
                    Mainloader=new FXMLLoader(getClass().getResource("/com/talk/view/main.fxml"));
                    root= Mainloader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Stage stage=(Stage)loginbtn.getScene().getWindow();
                Scene scene=new Scene((root));
                stage.setScene(scene);

                new StageMove(stage,scene.getRoot());

                stage.show();



                //将fxml放入map统一管理
                ManageMain.addMain(uname,Mainloader.getController());
                //1把上线好友信息发过来 2告知我的其他好友 我已上线
                Messagebean messagebean2 = new Messagebean();
                messagebean2.setReturnType("9");
                messagebean2.setSender(uname);

                Messagebean messagebean3 = new Messagebean();
                messagebean3.setReturnType("10");
                messagebean3.setSender(uname);


                Socket socket = ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messagebean2);

                    ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream1.writeObject(messagebean3);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("账号或者密码输入错误");
                alert.show();
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
