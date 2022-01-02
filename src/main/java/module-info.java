module com.mastercollider.stegofierfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires jcommander;
    requires com.jfoenix;


    opens com.mastercollider.stegofierfx to javafx.fxml;
    exports com.mastercollider.stegofierfx;
    exports com.mastercollider.stegofierfx.GUI;
    opens com.mastercollider.stegofierfx.GUI to javafx.fxml;
    exports com.mastercollider.stegofierfx.GUI.controllers;
    opens com.mastercollider.stegofierfx.GUI.controllers to javafx.fxml;
}
