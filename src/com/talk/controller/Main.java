package com.talk.controller;

import com.talk.bean.Messagebean;
import com.talk.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class Main implements Initializable {
    @FXML
    private ImageView addimg;

    @FXML
    private TextField friendnum;

    @FXML
    private Label loginLable;

    @FXML
    private ImageView imgview;

    @FXML
    private Label autograph;

    @FXML
    private ListView<Map<String, Object>> friendlist;

    @FXML
    private ListView<Map<String, Object>> grouplist;

    public static Map<String, Object> nowFriendInfo;

    public static Map<String, Object> userInfo;

    public static ArrayList<Map<String, Object>> groupNumber;
    public static String groupIndex;
    public static Map<String, Object> TogroupItemInfo;

    public ArrayList<Map<String, Object>> grouplists = null;
    @FXML
    void closeStage(MouseEvent event) {
        Stage mainStage = (Stage) autograph.getScene().getWindow();
        mainStage.close();
    }

    @FXML
    void minstage(MouseEvent event) {
        Stage mainStage = (Stage) autograph.getScene().getWindow();
        mainStage.setIconified(true);
    }

    public void updateimg(Messagebean messagebean) {

        //先弹窗
        String alertInfo = messagebean.getReturnres();

        String imgurl = messagebean.getImgurl();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText(alertInfo);
                alert.show();
                //更新头像

                Image image = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\" + imgurl);
                imgview.setImage(image);
            }
        });

    }

    @FXML
    void updateimg(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage selectFile = new Stage();

        //设置标题
        fileChooser.setTitle("单选文件");
        //设置打开初始地址
        fileChooser.setInitialDirectory(new File("D:\\files\\idea_workspace\\test11\\src\\com\\server\\img"));
        //过滤选择文件类型
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("图片类型", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(selectFile);
        if (file != null) {
            //返回路径  C:\Users\26321\Desktop\wechat\IMG_20200210_211200.jpg

            String fileUrl = String.valueOf(file);
            String[] sArray1 = fileUrl.split("\\\\");
            String imgurl = sArray1[sArray1.length - 1];
            Messagebean userAllInfo = Login.UserAllInfo;
            Map<String, Object> userinfo = userAllInfo.getUserinfo();
            String uname = (String) userinfo.get("uname");

            Messagebean messagebean = new Messagebean();
            messagebean.setReturnType("8");
            messagebean.setImgurl(imgurl);
            messagebean.setSender(uname);

            Messagebean messagebean2 = new Messagebean();
            messagebean2.setReturnType("11");
            messagebean2.setSender(uname);
            Socket socket = ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(messagebean);

                ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream1.writeObject(messagebean2);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    void addforg(MouseEvent event) {
        Parent root= null;
        FXMLLoader Wetalkloader=null;
        try {
            Wetalkloader=new FXMLLoader(getClass().getResource("/com/talk/view/addForG.fxml"));
            root= Wetalkloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.initStyle(StageStyle.UNDECORATED);
        new StageMove(stage,scene.getRoot());
        stage.show();
    }

    public void getItemNumber(ArrayList<Map<String, Object>> groupNumber) {
        this.groupNumber = groupNumber;
    }
    //点击好友打开聊天界面
//    public class NoticeListItemChangeListener implements ChangeListener<Object> {
//
//        @Override
//        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
//            System.out.println(oldValue+"-----oldValue");
//            System.out.println(newValue+"-----newValue");
//            System.out.println(observable+"-----observable");
//
//                System.out.println("我重新执行了一遍-------------------------------------");
//                //获取点击的索引
//                int frienditem=friendlist.getSelectionModel().getSelectedIndex();
//
//                System.out.println(frienditem+"获取点击的索引");
//
//                Messagebean userAllInfo= Login.UserAllInfo;
//
//                //通过索引获取好友信息
//                ArrayList<Map<String, Object>> friendlists=userAllInfo.getFriendsinfo();
//
//                //获取单个好友
//                Map<String, Object> nowFriend= friendlists.get(frienditem);
//                String nowFriendnickname = (String)nowFriend.get("nickname");
//                String nowFrienduname = (String)nowFriend.get("uname");
//
//                //获取用户信息
//                Map<String,Object> userinfo=userAllInfo.getUserinfo();
//                String usernickname = (String)userinfo.get("usernickname");
//                String uname = (String)userinfo.get("uname");
//
//
//                //将点击聊天朋友 赋值给全局变量 传给聊天界面
//                nowFriendInfo=nowFriend;
//                Parent root= null;
//                FXMLLoader Wetalkloader=null;
//                try {
//                    Wetalkloader=new FXMLLoader(getClass().getResource("/com/talk/view/Wetalk.fxml"));
//                    root= Wetalkloader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //将fxml放入map统一管理
//                System.out.println(uname+" "+nowFrienduname+"----------------------验证");
//                MangeScene.addWetalk(uname+" "+nowFrienduname,Wetalkloader.getController());
//                System.out.println(MangeScene.getNumber()+"-----------单聊hash数量");
//
//                Scene scene = new Scene(root);
//                Stage stage = new Stage();
//                stage.setTitle("你正在和"+nowFriendnickname+"聊天");
//                stage.setScene(scene);
//                stage.initStyle(StageStyle.UNDECORATED);
//                new StageMove(stage,scene.getRoot());
//                stage.show();
//        }
//    }

    //用来用来弹窗以及  是否增加好友列表
    public void IsAddfriend(Messagebean messagebean) {

        //先弹窗
        String alertInfo=messagebean.getReturnres();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText(alertInfo);
                alert.show();
            //是否刷新好友列表
                friendlist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
                    @Override
                    public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                        ListCell<Map<String, Object>> fricell = new ListCell<Map<String, Object>>() {
                            @Override
                            protected void updateItem(Map<String, Object> item, boolean empty) {
                                super.updateItem(item, empty);
                                //初始化节点
                                if (!empty) {
                                    ImageView imageView = new ImageView();
                                    Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\" + item.get("imgurl"));

                                    friendimage = pixWithImage(3, friendimage);
                                    imageView.setImage(friendimage);
                                    imageView.setFitHeight(50);
                                    imageView.setFitWidth(50);

                                    Rectangle rectangle1 = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
                                    rectangle1.setArcHeight(imageView.prefHeight(-1));
                                    rectangle1.setArcWidth(imageView.prefWidth(-1));
                                    imageView.setClip(rectangle1);

                                    imageView.setId("image");

                                    HBox hbox = new HBox(10);
                                    System.out.println((String) item.get("uname"));
                                    hbox.setId((String) item.get("uname"));
                                    Label friendnickname = new Label((String) (item.get("nickname")));
                                    Label friendautograph = new Label((String) (item.get("autograph")));

                                    friendnickname.setFont(new Font("System", 20));
                                    VBox vbox = new VBox();
                                    VBox.setMargin(friendnickname, new Insets(2));
                                    VBox.setMargin(friendautograph, new Insets(2));
                                    vbox.getChildren().addAll(friendnickname, friendautograph);
                                    hbox.getChildren().addAll(imageView, vbox);//添加
                                    this.setGraphic(hbox);
                                }

                            }
                        };
                        return fricell;
                    }
                });

                List<String> onload = messagebean.getOnloadstr();
                Messagebean userAllInfoMain = Login.UserAllInfo;
                ArrayList<Map<String, Object>> friendlists = userAllInfoMain.getFriendsinfo();
                Map<String, Object> friendinfo=messagebean.getUserinfo();

                if(friendinfo.size()!=0){
                    friendlists.add(friendinfo);
                    Login.UserAllInfo.setFriendsinfo(friendlists);
                    System.out.println(userAllInfoMain.getFriendsinfo().size()+"当前好友量");
                    ObservableList<Map<String, Object>> maps = FXCollections.observableArrayList(friendlists);
                    friendlist.setItems(maps);
                }
            }
        });

    }

    public void updateallimg(Messagebean messagebean) {

        Messagebean userAllInfo = Login.UserAllInfo;
        List<String> onload = messagebean.getOnloadstr();

        //通过索引获取好友信息
        ArrayList<Map<String, Object>> friendAlllist = messagebean.getFriendsinfo();

        Image image=null;
            for(int j=0;j<friendAlllist.size();j++)
            {
                int k=0;
                Map<String, Object> item=friendAlllist.get(j);
                String itemindex=(String) item.get("uname");
                ImageView selectImg = (ImageView)friendlist.lookup("#"+item.get("uname")).lookup("#image");
                for (int i=0;i<onload.size();i++){
                    if(itemindex.equals(onload.get(i))){
                        k++;
                    }
                }

                if(k>0){
                     image=pixWithImage(5,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+item.get("imgurl")));
                }else {
                     image=pixWithImage(3,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+item.get("imgurl")));

                }
                selectImg.setImage(image);


            }


//        List<String> onload=messagebean.getOnloadstr();
//
//        Messagebean userAllInfo= Login.UserAllInfo;
//        ArrayList<Map<String, Object>> friendlists=messagebean.getFriendsinfo();
//
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<friendlists.size();i++)
//                {
//                    friendlist.getItems().remove(0);
//                }
//
//                for(int i=0;i<friendlists.size();i++)
//                {
//                    Map<String, Object> friendItem=friendlists.get(i);
//
//                    String uname=(String)(friendItem.get("uname"));
//                    Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+friendItem.get("imgurl"));
//
//                    if(onload.size()==0){
//                        friendimage=pixWithImage(3,friendimage);
//                    }else {
//                        int k=0;
//                        for(int j = 0; j < onload.size(); j++) {
//                            if(uname.equals(onload.get(j))){
//                                k+=1;
//                            }}
//                        if(k==0){
//                            friendimage=pixWithImage(3,friendimage);
//                        }else {
//                            friendimage=pixWithImage(5,friendimage);
//                        }
//                    }
//
//                    //头像
//                    ImageView imageView = new ImageView();
//                    imageView.setImage(friendimage);
//                    imageView.setFitHeight(50);
//                    imageView.setFitWidth(50);
//
//                    Rectangle rectangle = new Rectangle(imageView.prefWidth(-1),imageView.prefHeight(-1));
//                    rectangle.setArcHeight(imageView.prefHeight(-1));
//                    rectangle.setArcWidth(imageView.prefWidth(-1));
//                    imageView.setClip(rectangle);
//
//
//                    HBox hbox=new HBox(10);
//
//
//                    Label friendnickname=new Label((String)(friendItem.get("nickname")));
//                    Label friendautograph=new Label((String)(friendItem.get("autograph")));
//
//
//                    friendnickname.setFont(new Font("System",20));
//                    VBox vbox=new VBox();
//                    VBox.setMargin(friendnickname, new Insets(2));
//                    VBox.setMargin(friendautograph, new Insets(2));
//                    vbox.getChildren().addAll(friendnickname,friendautograph);
//                    hbox.getChildren().addAll(imageView,vbox);//添加
//
//                    friendlist.getItems().add(hbox);
//                }
//            }
//        });

    }

    public void IsAddGroup(Messagebean messagebean) {

        //先弹窗
        String alertInfo=messagebean.getReturnres();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText(alertInfo);
                alert.show();

                grouplist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
                    @Override
                    public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                        ListCell<Map<String, Object>> groupcell = new ListCell<Map<String, Object>>() {
                            @Override
                            protected void updateItem(Map<String, Object> item, boolean empty) {
                                super.updateItem(item, empty);
                                //初始化节点
                                if (!empty) {
                                    ImageView imageViewGroup = new ImageView();

                                    Image groupItemimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\otherimg\\" + item.get("groupImgurl"));
//              groupItemimage=pixWithImage(3,groupItemimage);

                                    imageViewGroup.setImage(groupItemimage);
                                    imageViewGroup.setFitHeight(50);
                                    imageViewGroup.setFitWidth(50);
                                    imageViewGroup.setId("image");

                                    Rectangle rectangle1 = new Rectangle(imageViewGroup.prefWidth(-1), imageViewGroup.prefHeight(-1));
                                    rectangle1.setArcHeight(imageViewGroup.prefHeight(-1));
                                    rectangle1.setArcWidth(imageViewGroup.prefWidth(-1));
                                    imageViewGroup.setClip(rectangle1);

                                    HBox hboxGroup = new HBox(10);
                                    hboxGroup.setId((String) item.get("uname"));
                                    Label groupnickname = new Label((String) (item.get("groupName")));
                                    Label groupautograph = new Label((String) (item.get("groupAbout")));

                                    groupnickname.setFont(new Font("System", 20));

                                    VBox vboxgroup = new VBox();
                                    vboxgroup.setMargin(groupnickname, new Insets(2));
                                    vboxgroup.setMargin(groupautograph, new Insets(2));
                                    vboxgroup.getChildren().addAll(groupnickname, groupautograph);
                                    hboxGroup.getChildren().addAll(imageViewGroup, vboxgroup);//添加

                                    this.setGraphic(hboxGroup);
                                }
                            }
                        };
                        return groupcell;
                    }
                });
                Messagebean userAllInfo = Login.UserAllInfo;
                ArrayList<Map<String, Object>> grouplists = userAllInfo.getGroupsinfo();
                Map<String, Object> Newaddgroupinfo=messagebean.getNewaddgroupinfo();
                int index=grouplists.size();

                if(Newaddgroupinfo.size()!=0){
                    grouplists.add(Newaddgroupinfo);
                    Login.UserAllInfo.setGroupsinfo(grouplists);
                    ObservableList<Map<String, Object>> groupmap = FXCollections.observableArrayList(grouplists);
                    grouplist.setItems(groupmap);
                }
            }
        });

    }

    public void alertonline(List<String> onload) {
        Messagebean userAllInfo = Login.UserAllInfo;

        //通过索引获取好友信息
        ArrayList<Map<String, Object>> friendAlllist = userAllInfo.getFriendsinfo();
        Map<String, Object>  itemfri=new HashMap<>();

        for(int i=0;i<onload.size();i++)
        {
            for(int j=0;j<friendAlllist.size();j++)
            {
                if(onload.get(i).equals(friendAlllist.get(j).get("uname"))){
                    itemfri=friendAlllist.get(j);
                }
            }

            ImageView selectImg = (ImageView)friendlist.lookup("#"+onload.get(i)).lookup("#image");
            Image image=pixWithImage(5,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+itemfri.get("imgurl")));
            selectImg.setImage(image);
        }

//
//        Messagebean userAllInfo= Login.UserAllInfo;
//        ArrayList<Map<String, Object>> friendlists=userAllInfo.getFriendsinfo();
//
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<friendlists.size();i++)
//                {
//                    friendlist.getItems().remove(0);
//                }
//
//                for(int i=0;i<friendlists.size();i++)
//                {
////                    System.out.println(friendlists.size()+"好友数量--------------");
//                    Map<String, Object> friendItem=friendlists.get(i);
//
//                    String uname=(String)(friendItem.get("uname"));
//                    Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+friendItem.get("imgurl"));
////                    System.out.println(onload.size()+"在线人数---------------");
//
//
//                    if(onload.size()==0){
//                        friendimage=pixWithImage(3,friendimage);
//                    }else {
//                        int k=0;
//                        for(int j = 0; j < onload.size(); j++) {
//                            if(uname.equals(onload.get(j))){
//                                k+=1;
//                            }}
//                        if(k==0){
//                            friendimage=pixWithImage(3,friendimage);
//                        }else {
//                            friendimage=pixWithImage(5,friendimage);
//                        }
//                    }
//
//                    //头像
//                    ImageView imageView = new ImageView();
//                    imageView.setImage(friendimage);
//                    imageView.setFitHeight(50);
//                    imageView.setFitWidth(50);
//
//                    Rectangle rectangle = new Rectangle(imageView.prefWidth(-1),imageView.prefHeight(-1));
//                    rectangle.setArcHeight(imageView.prefHeight(-1));
//                    rectangle.setArcWidth(imageView.prefWidth(-1));
//                    imageView.setClip(rectangle);
//
//
//                    HBox hbox=new HBox(10);
//
//
//                    Label friendnickname=new Label((String)(friendItem.get("nickname")));
//                    Label friendautograph=new Label((String)(friendItem.get("autograph")));
//
//
//                    friendnickname.setFont(new Font("System",20));
//                    VBox vbox=new VBox();
//                    VBox.setMargin(friendnickname, new Insets(2));
//                    VBox.setMargin(friendautograph, new Insets(2));
//                    vbox.getChildren().addAll(friendnickname,friendautograph);
//                    hbox.getChildren().addAll(imageView,vbox);//添加
//
//                    friendlist.getItems().add(hbox);
//                }
//            }
//        });
    }

    public void updateNewfriend(List<String> onload) {
        Messagebean userAllInfoMain = Login.UserAllInfo;
        System.out.println("现在在执行我");
        //通过索引获取好友信息
        ArrayList<Map<String, Object>> friendAlllist = userAllInfoMain.getFriendsinfo();
        System.out.println(friendAlllist.size()+"----------------数量");
        System.out.println(onload);
        Image image=null;
        if(onload.size()!=0){
            for(int j=0;j<friendAlllist.size();j++)
            {
                int k=0;
                Map<String, Object> item=friendAlllist.get(j);
                String itemindex=(String) item.get("uname");
                System.out.println(itemindex+"--------friendAlllist  uname");
                ImageView selectImg = (ImageView)friendlist.lookup("#"+item.get("uname")).lookup("#image");
                for (int i=0;i<onload.size();i++){
                    System.out.println(onload.get(i)+"-------onload.get(i)");
                    if(itemindex.equals(onload.get(i))){
                        k++;
                    }
                }

                if(k>0){
                    System.out.println("k>o");
                    image=pixWithImage(5,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+item.get("imgurl")));
                }else {
                    System.out.println("k=o");
                    image=pixWithImage(3,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+item.get("imgurl")));

                }
                selectImg.setImage(image);


            }
        }


//        System.out.println(onload);
//
//        Messagebean userAllInfo= Login.UserAllInfo;
//        ArrayList<Map<String, Object>> friendlists=userAllInfo.getFriendsinfo();
//
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<friendlists.size();i++)
//                {
//                    friendlist.getItems().remove(0);
//                }
//
//                for(int i=0;i<friendlists.size();i++)
//                {
//                    System.out.println(friendlists.size()+"好友数量--------------");
//                    Map<String, Object> friendItem=friendlists.get(i);
//
//                    String uname=(String)(friendItem.get("uname"));
//                    Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+friendItem.get("imgurl"));
//                    System.out.println(onload);
//                    System.out.println("------------------onload-------------------");
//                        if(onload.size()==0){
//                            friendimage=pixWithImage(3,friendimage);
//                        }else {
//                            int k=0;
//                            for(int j = 0; j < onload.size(); j++) {
//                                if(uname.equals(onload.get(j))){
//                                    k+=1;
//                                }}
//                            if(k==0){
//                                friendimage=pixWithImage(3,friendimage);
//                            }else {
//                                friendimage=pixWithImage(5,friendimage);
//                            }
//                        }
//
//                        //头像
//                        ImageView imageView = new ImageView();
//                        imageView.setImage(friendimage);
//                        imageView.setFitHeight(50);
//                        imageView.setFitWidth(50);
//
//                        Rectangle rectangle = new Rectangle(imageView.prefWidth(-1),imageView.prefHeight(-1));
//                        rectangle.setArcHeight(imageView.prefHeight(-1));
//                        rectangle.setArcWidth(imageView.prefWidth(-1));
//                        imageView.setClip(rectangle);
//
//                        HBox hbox=new HBox(10);
//
//
//                        Label friendnickname=new Label((String)(friendItem.get("nickname")));
//                        Label friendautograph=new Label((String)(friendItem.get("autograph")));
//
//
//                        friendnickname.setFont(new Font("System",20));
//                        VBox vbox=new VBox();
//                        VBox.setMargin(friendnickname, new Insets(2));
//                        VBox.setMargin(friendautograph, new Insets(2));
//                        vbox.getChildren().addAll(friendnickname,friendautograph);
//                        hbox.getChildren().addAll(imageView,vbox);//添加
//
//                        friendlist.getItems().add(hbox);
//                    }
//
//
//
//                }
//        });
    }

    public void updateOnLoad(List<String> onload) {
            System.out.println("当前上线");
            System.out.println(onload);
            Messagebean userAllInfo = Login.UserAllInfo;
            ArrayList<Map<String, Object>> friendlists = userAllInfo.getFriendsinfo();

              //通过索引获取好友信息
              ArrayList<Map<String, Object>> friendAlllist = userAllInfo.getFriendsinfo();
               Map<String, Object>  itemfri=new HashMap<>();

            for(int i=0;i<onload.size();i++)
               {
                   for(int j=0;j<friendAlllist.size();j++)
                   {
                       if(onload.get(i).equals(friendAlllist.get(j).get("uname"))){
                           itemfri=friendAlllist.get(j);
                       }
                   }

                   ImageView selectImg = (ImageView)friendlist.lookup("#"+onload.get(i)).lookup("#image");
                   Image image=pixWithImage(5,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+itemfri.get("imgurl")));
                   selectImg.setImage(image);
               }

    }

    public Image pixWithImage(int type, Image image) {
        PixelReader pixelReader = image.getPixelReader();
        if (image.getWidth() > 0 && image.getHeight() > 0) {
            WritableImage wImage;
            wImage = new WritableImage(
                    (int) image.getWidth(),
                    (int) image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    switch (type) {
                        case 1:
                            // 颜色变轻
                            color = color.brighter();
                            break;
                        case 2:
                            // 颜色变深
                            color = color.darker();
                            break;
                        case 3:
                            // 灰度化
                            color = color.grayscale();
                            break;
                        case 4:
                            // 颜色反转
                            color = color.invert();
                            break;
                        case 5:
                            // 颜色饱和
                            color = color.saturate();
                            break;
                        case 6:
                            // 颜色不饱和
                            color = color.desaturate();
                            break;
                        case 7:
                            // 颜色灰度化后反转（字黑体，背景鲜亮，可用于强字弱景）
                            color = color.grayscale();
                            color = color.invert();
                            break;
                        default:
                            break;
                    }

                    pixelWriter.setColor(x, y, color);
                }
            }
            return wImage;
        }
        return null;
    }

    @FXML
    void clickfrienditem(MouseEvent event) {
        Map<String, Object> selectedItem = friendlist.getSelectionModel().getSelectedItem();
        String uname=(String)selectedItem.get("uname");

        int frienditem = friendlist.getSelectionModel().getSelectedIndex();
        System.out.println(frienditem + "获取点击的索引");


        Messagebean userAllInfo = Login.UserAllInfo;

        //通过索引获取好友信息
        ArrayList<Map<String, Object>> friendlists = userAllInfo.getFriendsinfo();

        //获取单个好友
        Map<String, Object> nowFriend = friendlists.get(frienditem);
        String nowFrienduname = (String) nowFriend.get("uname");

        //获取用户信息
        Map<String, Object> userinfo = userAllInfo.getUserinfo();
        String username = (String) userinfo.get("uname");

        //将点击聊天朋友 赋值给全局变量 传给聊天界面
        nowFriendInfo = nowFriend;
        Parent root = null;
        FXMLLoader Wetalkloader = null;
        try {
            Wetalkloader = new FXMLLoader(getClass().getResource("/com/talk/view/Wetalk.fxml"));
            root = Wetalkloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将fxml放入map统一管理
        MangeScene.addWetalk(username + " " + nowFrienduname, Wetalkloader.getController());


        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        new StageMove(stage, scene.getRoot());
        stage.show();
    }

    @FXML
    void clickgroupitem(MouseEvent event) {
        Map<String, Object> selectedItem = grouplist.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem.get("groupid"));
        //获得点击的索引
                Messagebean userAllInfo = Login.UserAllInfo;
                 ArrayList<Map<String, Object>> grouplists =userAllInfo.getGroupsinfo();
                int groupitem=grouplist.getSelectionModel().getSelectedIndex();

                System.out.println(groupitem + "获取点击的索引");
                //通过索引来获得该群详细信息

                Map<String, Object> groupItemInfo= grouplists.get(groupitem);

                int groupindex=Integer.parseInt((String)groupItemInfo.get("groupid"));
                groupIndex=groupindex+"";
                TogroupItemInfo=groupItemInfo;

                Map<String,Object> userinfo=userAllInfo.getUserinfo();
                String uname = (String)userinfo.get("uname");

                //通过索引进行查找
                Messagebean messagebean = new Messagebean();
                messagebean.setReturnType("3");
                messagebean.setGroupindex(groupindex);
                messagebean.setSender(uname);

                Socket socket = ManageClientConServerThread.getClientConServerThreadByuname(uname).getSocket();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messagebean);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /**
                 * 每次开一个界面  都放入hashmap中
                 */
                try {
                    Parent root = null;
                    FXMLLoader grouptalkloader = null;
                    grouptalkloader = new FXMLLoader(getClass().getResource("/com/talk/view/grouptalk.fxml"));
                    root = grouptalkloader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    new StageMove(stage, scene.getRoot());
                    System.out.println(uname + " " + groupindex + "---------------放入客户端MAP");
                    //将fxml放入map统一管理
                    ManageGroup.addGrouptalk(groupindex + " ", grouptalkloader.getController());
                    stage.setScene(scene);
                    stage.show();
                   }
                catch (IOException e) {}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //点击事件
        // 调用上一个controller

        Messagebean userAllInfo = Login.UserAllInfo;

        //设置个人信息

        Map<String, Object> userinfo = userAllInfo.getUserinfo();

        Image image = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\" + userinfo.get("imgurl"));
//        image=pixWithImage(3,image);
        imgview.setImage(image);
        Rectangle rectangle = new Rectangle(imgview.prefWidth(-1), imgview.prefHeight(-1));
        rectangle.setArcHeight(imgview.prefHeight(-1));
        rectangle.setArcWidth(imgview.prefWidth(-1));
        imgview.setClip(rectangle);

        //将当前登录用户赋值给全局变量 传给聊天界面
        userInfo = userinfo;

        String usernickname = (String) userinfo.get("nickname");
        String userautograph = (String) userinfo.get("autograph");
        loginLable.setText(usernickname); //设置登录用户昵称
        autograph.setText(userautograph); //设置登录用户个性签名


        grouplist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
            @Override
            public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                ListCell<Map<String, Object>> groupcell = new ListCell<Map<String, Object>>() {
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        //初始化节点
                        if (!empty) {
                            ImageView imageViewGroup = new ImageView();

                            Image groupItemimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\otherimg\\" + item.get("groupImgurl"));

                            imageViewGroup.setImage(groupItemimage);
                            imageViewGroup.setFitHeight(50);
                            imageViewGroup.setFitWidth(50);
                            imageViewGroup.setId("image");

                            Rectangle rectangle1 = new Rectangle(imageViewGroup.prefWidth(-1), imageViewGroup.prefHeight(-1));
                            rectangle1.setArcHeight(imageViewGroup.prefHeight(-1));
                            rectangle1.setArcWidth(imageViewGroup.prefWidth(-1));
                            imageViewGroup.setClip(rectangle1);

                            HBox hboxGroup = new HBox(10);
                            hboxGroup.setId((String) item.get("uname"));
                            Label groupnickname = new Label((String) (item.get("groupName")));
                            Label groupautograph = new Label((String) (item.get("groupAbout")));

                            groupnickname.setFont(new Font("System", 20));

                            VBox vboxgroup = new VBox();
                            vboxgroup.setMargin(groupnickname, new Insets(2));
                            vboxgroup.setMargin(groupautograph, new Insets(2));
                            vboxgroup.getChildren().addAll(groupnickname, groupautograph);
                            hboxGroup.getChildren().addAll(imageViewGroup, vboxgroup);//添加

                            this.setGraphic(hboxGroup);
                        }
                    }
                };
                return groupcell;
            }
        });

        ArrayList<Map<String, Object>> grouplists = userAllInfo.getGroupsinfo();

        ObservableList<Map<String, Object>> groupmap = FXCollections.observableArrayList(grouplists);
        grouplist.setItems(groupmap);


        friendlist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
            @Override
            public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                ListCell<Map<String, Object>> fricell = new ListCell<Map<String, Object>>() {
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        //初始化节点
                        if (!empty) {
                            ImageView imageView = new ImageView();
                            Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\" + item.get("imgurl"));

                            friendimage = pixWithImage(3, friendimage);
                            imageView.setImage(friendimage);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);

                            Rectangle rectangle1 = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
                            rectangle1.setArcHeight(imageView.prefHeight(-1));
                            rectangle1.setArcWidth(imageView.prefWidth(-1));
                            imageView.setClip(rectangle1);

                            imageView.setId("image");

                            HBox hbox = new HBox(10);
                            System.out.println((String) item.get("uname"));
                            hbox.setId((String) item.get("uname"));
                            Label friendnickname = new Label((String) (item.get("nickname")));
                            Label friendautograph = new Label((String) (item.get("autograph")));

                            friendnickname.setFont(new Font("System", 20));
                            VBox vbox = new VBox();
                            VBox.setMargin(friendnickname, new Insets(2));
                            VBox.setMargin(friendautograph, new Insets(2));
                            vbox.getChildren().addAll(friendnickname, friendautograph);
                            hbox.getChildren().addAll(imageView, vbox);//添加
                            this.setGraphic(hbox);
                        }

                    }
                };
                return fricell;
            }
        });

        ArrayList<Map<String, Object>> friendlists = userAllInfo.getFriendsinfo();

        ObservableList<Map<String, Object>> maps = FXCollections.observableArrayList(friendlists);
        friendlist.setItems(maps);

    }

    public void friendAddfriend(Messagebean msbean) {
        //先弹窗

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //是否刷新好友列表
                friendlist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
                    @Override
                    public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                        ListCell<Map<String, Object>> fricell = new ListCell<Map<String, Object>>() {
                            @Override
                            protected void updateItem(Map<String, Object> item, boolean empty) {
                                super.updateItem(item, empty);
                                //初始化节点
                                if (!empty) {
                                    ImageView imageView = new ImageView();
                                    Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\" + item.get("imgurl"));

                                    friendimage = pixWithImage(3, friendimage);
                                    imageView.setImage(friendimage);
                                    imageView.setFitHeight(50);
                                    imageView.setFitWidth(50);

                                    Rectangle rectangle1 = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
                                    rectangle1.setArcHeight(imageView.prefHeight(-1));
                                    rectangle1.setArcWidth(imageView.prefWidth(-1));
                                    imageView.setClip(rectangle1);

                                    imageView.setId("image");

                                    HBox hbox = new HBox(10);
                                    System.out.println((String) item.get("uname"));
                                    hbox.setId((String) item.get("uname"));
                                    Label friendnickname = new Label((String) (item.get("nickname")));
                                    Label friendautograph = new Label((String) (item.get("autograph")));

                                    friendnickname.setFont(new Font("System", 20));
                                    VBox vbox = new VBox();
                                    VBox.setMargin(friendnickname, new Insets(2));
                                    VBox.setMargin(friendautograph, new Insets(2));
                                    vbox.getChildren().addAll(friendnickname, friendautograph);
                                    hbox.getChildren().addAll(imageView, vbox);//添加
                                    this.setGraphic(hbox);
                                }

                            }
                        };
                        return fricell;
                    }
                });

                List<String> onload = msbean.getOnloadstr();
                Messagebean userAllInfoMain = Login.UserAllInfo;
                ArrayList<Map<String, Object>> friendlists = userAllInfoMain.getFriendsinfo();
                Map<String, Object> friendinfo=msbean.getUserinfo();

                if(friendinfo.size()!=0){

                    int index=friendlists.size()-1;
                    if(!friendlists.get(index).equals(friendinfo)){
                        friendlists.add(friendinfo);
                        Login.UserAllInfo.setFriendsinfo(friendlists);
                        System.out.println(userAllInfoMain.getFriendsinfo().size()+"当前好友量");
                        ObservableList<Map<String, Object>> maps = FXCollections.observableArrayList(friendlists);
                        friendlist.setItems(maps);
                    }
                }
            }
        });
    }
}
