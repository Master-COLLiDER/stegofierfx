/*******************************************************************************
 *  This file is made by Probal D. Saikia on 3/1/2022
 *  https://github.com/Master-COLLiDER
 *  NOTICE: This file is subject to the terms and conditions defined
 * in the file 'LICENSE' which is part of this source code package.
 ******************************************************************************/

package com.mastercollider.stegofierfx.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EncoderFX extends Application {
    private double xOffset, yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try{
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image(EncoderFX.class.getResourceAsStream("logo_250x250.png"));

            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("StegofierFX 1.0");

        }catch (Exception e){
            e.printStackTrace();
        }
        display(primaryStage);
    }

    public void display(Stage stage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("EncoderLayout.fxml"));
            stage.setScene(new Scene(root));


                root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
