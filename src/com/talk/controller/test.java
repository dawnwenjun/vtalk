package com.talk.controller;

import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import com.talk.bean.Messagebean;
import com.talk.utils.MangeScene;
import com.talk.utils.StageMove;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class test  implements Initializable {

    @FXML
    private ListView<Map<String, Object>> friendlist;



    @FXML
    void clickitem(MouseEvent event) {
        Map<String, Object> selectedItem = friendlist.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem.get("uname"));

        Parent root= null;
        FXMLLoader Wetalkloader=null;
        try {
            Wetalkloader=new FXMLLoader(getClass().getResource("/com/talk/view/Wetalk.fxml"));
            root= Wetalkloader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("你正在和"+selectedItem.get("uname")+"聊天");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            new StageMove(stage,scene.getRoot());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void clickbtn(MouseEvent event) {
        ImageView selectImg = (ImageView)friendlist.lookup("#0404").lookup("#image");
        Image image=pixWithImage(5,new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\qq.jpg"));
        selectImg.setImage(image);
    }




    public Image pixWithImage(int type,Image image){
        PixelReader pixelReader = image.getPixelReader();
        if(image.getWidth()>0 && image.getHeight() >0){
            WritableImage wImage;
            wImage = new WritableImage(
                    (int)image.getWidth(),
                    (int)image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {



        friendlist.setCellFactory(new Callback<ListView<Map<String, Object>>, ListCell<Map<String, Object>>>() {
            @Override
            public ListCell<Map<String, Object>> call(ListView<Map<String, Object>> param) {
                ListCell<Map<String, Object>> fricell=new ListCell<Map<String, Object>>(){
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        //初始化节点
                        if(!empty){
                            ImageView imageView = new ImageView();
                            Image friendimage = new Image("file:D:\\files\\idea_workspace\\test11\\src\\com\\server\\img\\"+item.get("imgurl"));

                            friendimage=pixWithImage(3,friendimage);
                            imageView.setImage(friendimage);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);

                            imageView.setId("image");

                            HBox hbox=new HBox(10);
                            System.out.println((String)item.get("uname"));
                            hbox.setId((String)item.get("uname"));
                            Label friendnickname=new Label((String)(item.get("nickname")));
                            Label friendautograph=new Label((String)(item.get("autograph")));

                            friendnickname.setFont(new Font("System",20));
                            VBox vbox=new VBox();
                            VBox.setMargin(friendnickname, new Insets(2));
                            VBox.setMargin(friendautograph, new Insets(2));
                            vbox.getChildren().addAll(friendnickname,friendautograph);
                            hbox.getChildren().addAll(imageView,vbox);//添加
                            this.setGraphic(hbox);
                        }


                    }
                };
                return fricell;
            }
        });

        Messagebean userAllInfo= Login.UserAllInfo;
        ArrayList<Map<String, Object>> friendlists=userAllInfo.getFriendsinfo();

        ObservableList<Map<String, Object>> maps = FXCollections.observableArrayList(friendlists);
        friendlist.setItems(maps);
    }
}
