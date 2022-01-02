/*******************************************************************************
 *  This file is made by Probal D. Saikia on 3/1/2022
 *  https://github.com/Master-COLLiDER
 *  NOTICE: This file is subject to the terms and conditions defined
 * in the file 'LICENSE' which is part of this source code package.
 ******************************************************************************/

package com.mastercollider.stegofierfx;


import com.mastercollider.stegofierfx.GUI.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
