package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.view.ManagerView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LoginController implements Initializable {
    @FXML
    private StackPane rootPane;

    @FXML
    private Pane imagePane;

    @FXML
    private StackPane centerPane;

    @FXML
    private HBox loginPane;

    @FXML
    private JFXProgressBar lodingBar;

    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXPasswordField passWordTextField;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private Hyperlink registeredLink;

    @FXML
    private Label errorLabel;

    @FXML
    private Label userIcon;

    @FXML
    private Label pwdIcon;

    @FXML
    private JFXButton rightTopCloseBtn;

    /**
     * 登录
     * @param event
     */
    @FXML
    void loginBtnAction(ActionEvent event) throws IOException {
        MovieClientApplication.showView(ManagerView.class);
    }

    /**
     * 退出系统
     * @param event
     */
    @FXML
    void rightTopCloseButtonAction(ActionEvent event) {
        System.exit(0);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
