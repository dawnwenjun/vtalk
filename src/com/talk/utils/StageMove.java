package com.talk.utils;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class StageMove {
    private double xOffset;
    private double yOffset;
    private Stage stage;
    private Parent root;

    public StageMove(Stage stage, Parent root) {
        this.stage = stage;
        this.root = root;
        move();
    }

    private void move() {
        root.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
            root.setCursor(Cursor.CLOSED_HAND);
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        root.setOnMouseReleased(event -> {
            root.setCursor(Cursor.DEFAULT);
        });
    }
}
