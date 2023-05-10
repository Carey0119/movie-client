package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXAlert;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MovieDetailController implements Initializable {
    @FXML
    private TextArea movieDescTestArea;
    private JFXAlert jfxAlert;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setDesc(String desc){
        movieDescTestArea.setWrapText(true);
        movieDescTestArea.setText(desc);
    }
    public void setAlertView(JFXAlert alert){
        jfxAlert = alert;
    }
    @FXML
    void closeDetailButtonAction(javafx.event.ActionEvent actionEvent){
        jfxAlert.close();
    }

}
