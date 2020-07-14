package com.talk.controller;

import com.talk.bean.Messagebean;
import com.talk.utils.ManageClientConServerThread;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class Grouptalk  implements Initializable {
    @FXML
    private ListView<Object> groupnumberlist;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private FlowPane messagesList;
    @FXML
    private Label groupname;
    @FXML
    private TextArea messagetext;

    @FXML
    private Button msgbtn;

    @FXML
    private Label groupnumberlabel;

    @FXML
    private TextArea talklist;


    @FXML
    void minstage(MouseEvent event) {
        Stage groupStage = (Stage) groupnumberlabel.getScene().getWindow();
        groupStage.setIconified(true);
    }


    @FXML
    void closestage(MouseEvent event) {
        Stage groupStage = (Stage) groupnumberlabel.getScene().getWindow();
        groupStage.close();
    }

    public void setMessage(Messagebean messagebean){
        /**
         *
         * 来一条消息
         * 每个消息单元，消息单元使用HBox完美解决，目前每个单元里包含头像，消息两个部分，
         * 根据是否是自己发出的消息判断得到消息单元内的组件排列顺序以及对齐方式就可以得到靠左靠右的效果。
         *
         * 最后的部分就是这个气泡了，基于Label组件，添加圆角效果-fx-background-radius: 8px;，
         * 添加背景颜色-fx-background-color: rgb(179,231,244);就已经差不多了
         *
         * 三角，三角可以直接使用JavaFx的图形组件Polygon制作一个三角形并调整相对位置，
         *
         */
        //获取发送者 姓名 头像 发送内容


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox hBox = new HBox();
                ImageView imageView = new ImageView();

                Map<String, Object> NumberItem=messagebean.getUserinfo();

                Image Numberimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+NumberItem.get("imgurl"));

                imageView.setImage(Numberimage);
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);

                VBox vBox = new VBox();
                vBox.setPadding(new Insets(2, 0, 0, 8));

                Label Numbernickname=new Label((String)(NumberItem.get("nickname")));
                String username=(String) NumberItem.get("uname");


                Label NumberList=new Label((String)(messagebean.getReturnContext()));
                NumberList.setWrapText(true);
                NumberList.setMaxWidth(250);
                NumberList.setPadding(new Insets(6));
                NumberList.setFont(new Font(14));

                vBox.getChildren().addAll(Numbernickname,NumberList);

                hBox.setPrefWidth(580);
                //如果是自己发的messageBox.setAlignment(Pos.TOP_RIGHT);
                Map<String,Object> userInfo=Main.userInfo;
                String uname=(String)userInfo.get("uname"); //当前用户
                hBox.setPadding(new Insets(10, 5, 10, 5));
                hBox.setSpacing(8);
                vBox.setSpacing(5);
//                System.out.println(uname+"本地");

                System.out.println(username+"从服务端获取");
                if(username.equals(uname)){
                    NumberList.setStyle("-fx-background-color: rgb(38, 131, 245); -fx-background-radius: 8px;");
                    NumberList.setTextFill(Color.web("#ffffff"));
                    vBox.setAlignment(Pos.TOP_RIGHT);
                    hBox.getChildren().addAll(vBox,imageView);
                    hBox.setAlignment(Pos.TOP_RIGHT);
                    messagesList.getChildren().addAll(hBox);
                }else
                {
                    NumberList.setStyle("-fx-background-color: rgb(226, 192, 192); -fx-background-radius: 8px;");

                    hBox.getChildren().addAll(imageView,vBox);
                    messagesList.getChildren().addAll(hBox);

                }
            }
        });
    }




    @FXML
    void sendmsg(MouseEvent event) {
        Map<String,Object> userInfo=Main.userInfo;
        String uname=(String)userInfo.get("uname"); //当前用户

        String groupindex=Main.groupIndex;

        //当前用户正在群里面聊天
        Messagebean msbean=new Messagebean();

        //设置发送者
        msbean.setSender(uname);
        //设置接收者
        msbean.setRecipient(groupindex);
        //设置发送时间
        msbean.setSendTime(new java.util.Date().toString());
        // 设置发送内容
        msbean.setReturnContext(messagetext.getText());
        //设置发送信息类型
        msbean.setReturnType("4");
        Socket socket= ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();

        messagetext.setText("");
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(msbean);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean last = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //初始化
        ArrayList<Map<String, Object>> groupNumbers=Main.groupNumber;
        groupnumberlabel.setText("群成员"+groupNumbers.size());

        Map<String,Object> userInfo=Main.userInfo;
        String uname=(String)userInfo.get("uname"); //当前用户

        Map<String, Object>  groupinfo=Main.TogroupItemInfo;
        groupname.setText((String)groupinfo.get("groupName")+"("+(String)groupinfo.get("groupNum")+")");

        for(int i=0;i<groupNumbers.size();i++)
        {
            Map<String, Object> NumberItem=groupNumbers.get(i);


            //头像
            ImageView imageView = new ImageView();

            Image Numberimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+NumberItem.get("imgurl"));

            imageView.setImage(Numberimage);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);

            HBox hbox=new HBox(10);


            Label Numbernickname=new Label((String)(NumberItem.get("nickname")));


            Numbernickname.setFont(new Font("System",15));


            hbox.getChildren().addAll(imageView,Numbernickname);

            groupnumberlist.getItems().add(hbox);
        }

        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (last) {
                    scrollPane.setVvalue(1.0);
                    last = false;
                }
            }
        });

    }
}
