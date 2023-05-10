package com.myjavafx.movieclient.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RegexValidator;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.http.DelayUtil;
import com.myjavafx.movieclient.http.HttpApiService;
import com.myjavafx.movieclient.http.ResultVO;
import com.myjavafx.movieclient.view.LoginView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import org.springframework.util.DigestUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ResourceBundle;


@FXMLController
public class RegisterController implements Initializable {
    @FXML
    private StackPane rootPane;

    @FXML
    private Pane imagePane;

    @FXML
    private StackPane centerPane;

    @FXML
    private JFXProgressBar loadingBar;

    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXTextField ageTextField;

    @FXML
    private JFXComboBox sexComboBox;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXPasswordField rePasswordTextField;

    @FXML
    private JFXTextField addressTextField;
    @FXML
    private RegexValidator rePasswordRegexValidator;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton registerBtn;
    @FXML
    private Label errorLabel;

    @FXML
    void backBtnAction(ActionEvent event) {
        MovieClientApplication.showView(LoginView.class);
    }

    @FXML
    void registerBtnAction(ActionEvent event) {


        if (userNameTextField.validate() &&
                ageTextField.validate() &&
                sexComboBox.validate() &&
                passwordTextField.validate() &&
                rePasswordTextField.validate() &&
                addressTextField.validate()
        ) {
            centerPane.setDisable(true);
            String username = userNameTextField.getText();
            String password = passwordTextField.getText();
            String sex = sexComboBox.getSelectionModel().getSelectedIndex() == 0 ? "男":"女";
            String age = ageTextField.getText();
            String address = addressTextField.getText();
            password = DigestUtils.md5DigestAsHex((password + username).getBytes()).toLowerCase();
            Call<ResultVO> resultVOCall = HttpApiService.getInstance().httpApi.userRegister(username, password, age, sex, address);
            resultVOCall.enqueue(new Callback<ResultVO>() {
                @Override
                public void onResponse(Call<ResultVO> call, Response<ResultVO> response) {
                    if (response.body().getCode() == 1){
                        DelayUtil.delayRunToUIThread(1500, new Runnable() {
                            @Override
                            public void run() {
                                JFXDialogLayout layout = new JFXDialogLayout();
                                Label label = new Label("注册成功");
                                layout.setBody(label);
                                JFXAlert<Void> alert = new JFXAlert<>(MovieClientApplication.myStage);
                                alert.setOverlayClose(true);
                                alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
                                alert.setContent(layout);
                                alert.initModality(Modality.NONE);
                                alert.show();
                                MovieClientApplication.showView(LoginView.class);
                                centerPane.setDisable(false);
                            }
                        });
                    }else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                centerPane.setDisable(false);
                                errorLabel.setText(response.body().getMsg());
                            }
                        });

                    }

                }


                @Override
                public void onFailure(Call<ResultVO> call, Throwable throwable) {
                    // 为显示动效，模拟网络延迟1.5s
                    DelayUtil.delayRunToUIThread(1500, new Runnable() {
                        @Override
                        public void run() {
                            centerPane.setDisable(false);
                            errorLabel.setText(throwable.getMessage());
                        }
                    });
                    System.out.println(throwable.toString());
                }
            });
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 当右侧为不可编辑时，显示loading
        loadingBar.visibleProperty().bind(centerPane.disableProperty());
        loadingBar.managedProperty().bind(loadingBar.visibleProperty());
        // errorLabel 有值时才显示错误提示
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());
        userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            userNameTextField.validate();
        });
        ageTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            ageTextField.validate();
        });
        sexComboBox.focusColorProperty().addListener((o, oldVal, newVal) -> {
            sexComboBox.validate();
        });
        passwordTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            passwordTextField.validate();
        });
        rePasswordTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            rePasswordRegexValidator.setRegexPattern("^" + passwordTextField.getText() + "$");
            rePasswordTextField.validate();
        });
        addressTextField.focusedProperty().addListener((o, oldVal, newVal) -> {

        });
    }
}
