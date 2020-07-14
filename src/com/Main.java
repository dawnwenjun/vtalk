package com;

import com.talk.utils.StageMove;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/talk/view/login.fxml"));
        primaryStage.setTitle("VTalk");
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add( new Image("file:E:\\chormeDownload\\v.png"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        new StageMove(primaryStage,scene.getRoot());
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
