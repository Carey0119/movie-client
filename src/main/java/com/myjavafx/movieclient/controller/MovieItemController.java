package com.myjavafx.movieclient.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MovieItemController implements Initializable {
    @FXML
    private ImageView mainPicImageView;

    @FXML
    private Label moveNameLabel;

    @FXML
    private Label scoreLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void viewMoreButtonAction(ActionEvent actionEvent){

    }
    public void setViewModel(String obj){
        System.out.println(obj);
        moveNameLabel.setText(obj);
    }

}
