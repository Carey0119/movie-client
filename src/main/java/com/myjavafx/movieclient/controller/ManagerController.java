package com.myjavafx.movieclient.controller;

import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.utils.ScreenUtil;
import com.myjavafx.movieclient.enums.ManagerEnumView;
import com.myjavafx.movieclient.utils.SpringFXMLLoader;
import com.myjavafx.movieclient.utils.SpringUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class ManagerController implements Initializable {
    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox views;

    @FXML
    private HBox statisticsBox;

    @FXML
    private HBox movieManageHBox;

    @FXML
    private HBox userHBox;
    @FXML
    private ScrollPane body;

    @FXML
    void managerHBoxOnMouseClick(MouseEvent event) {
        updateBody(ManagerEnumView.MODULE_MOVIE_MANAGE);
    }

    @FXML
    void statisticsBoxMouseClicked(MouseEvent event) {
        updateBody(ManagerEnumView.MODULE_STATISTICS);
    }

    @FXML
    void userHBoxOnMouseClicked(MouseEvent event) {
        updateBody(ManagerEnumView.MODULE_USER_MANAGE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MovieClientApplication.myStage.setWidth(ScreenUtil.getFitWidth(1000,0.9));
        MovieClientApplication.myStage.setHeight(ScreenUtil.getFitHeight(600,0.8));
        MovieClientApplication.myStage.centerOnScreen();
    }

    private void updateBody(ManagerEnumView view) {
        try {
            Parent parent = SpringUtils.getBean(SpringFXMLLoader.class).load(view.fxmlPath);
            body.setContent(parent);
        } catch (IOException e) {
            //log.error(e);
        }
    }
}