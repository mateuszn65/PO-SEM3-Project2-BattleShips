package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    //MULTI-USE ALERT BOX
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);

        Label label = new Label(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(label, closeButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }
}
