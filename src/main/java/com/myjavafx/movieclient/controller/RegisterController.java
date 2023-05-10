package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.view.LoginView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;


@FXMLController
public class RegisterController {
    @FXML
    private StackPane rootPane;

    @FXML
    private Pane imagePane;

    @FXML
    private StackPane centerPane;

    @FXML
    private JFXProgressBar lodingBar;

    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton registerBtn;

    @FXML
    void backBtnAction(ActionEvent event) {
        MovieClientApplication.showView(LoginView.class);
    }

    @FXML
    void registerBtnAction(ActionEvent event) {
        MovieClientApplication.showView(LoginView.class);
    }


}
