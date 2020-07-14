package com.talk.controller;


import com.server.dao.userDao;
import com.talk.utils.StageMove;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Register {

    @FXML
    private Label returnLoginLabel;

    @FXML
    private Button loginbtn;

    @FXML
    private TextField username;

    @FXML
    private TextField nickname;

    @FXML
    private TextField userpassword;

    @FXML
    private TextField userautograph;

    @FXML
    void minStage(MouseEvent event) {

    }

    @FXML
    void closeStage(MouseEvent event) {

    }



    @FXML
    void onLogin(MouseEvent event) throws IOException {
        //获取账号密码
        String uname= username.getText();

        String upassword=userpassword.getText();

        String nname=nickname.getText();

        String autograph=userautograph.getText();

        System.out.println(uname);
        System.out.println(upassword);
        if(uname.length() == 0||upassword.length()==0||nname.length()==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("账号,密码,昵称缺一不可");
            alert.showAndWait();
        }else
        {
            userDao userdao=new userDao();
            int isregister=userdao.register(uname,upassword,nname,autograph);
            if(isregister==1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("注册成功");
                alert.showAndWait();
                Parent root=FXMLLoader.load(getClass().getResource("/com/talk/view/login.fxml"));
                Stage stage=(Stage)loginbtn.getScene().getWindow();
                Scene scene=new Scene((root));
                stage.setScene(scene);
            }else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("该用户已注册");
                alert.showAndWait();
            }

        }
    }

    @FXML
    void onReturnLogin(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/com/talk/view/login.fxml"));
        Stage stage=(Stage)returnLoginLabel.getScene().getWindow();
        Scene scene=new Scene((root));
        stage.setScene(scene);
        new StageMove(stage,scene.getRoot());
    }

}
