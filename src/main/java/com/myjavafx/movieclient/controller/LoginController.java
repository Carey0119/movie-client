package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.constant.RoleConstant;
import com.myjavafx.movieclient.http.*;
import com.myjavafx.movieclient.view.ClientView;
import com.myjavafx.movieclient.view.ManagerView;
import com.myjavafx.movieclient.view.RegisterView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.util.DigestUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private JFXProgressBar loadingBar;

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
     *
     * @param event
     */
    @FXML
    void loginBtnAction(ActionEvent event) throws IOException, InterruptedException {
        String username = userNameTextField.getText();
        String password = passWordTextField.getText();
        // 1、判断参数是否正确
        if (userNameTextField.validate() && passWordTextField.validate()) {
            // 2、内容不可编辑
            centerPane.setDisable(true);
            // 3、发起网络请求
            password = DigestUtils.md5DigestAsHex((password + username).getBytes()).toLowerCase();
            userLoginFromHttp(username, password);
        }
    }

    @FXML
    void registeredLinkAction(ActionEvent event) {
        MovieClientApplication.showView(RegisterView.class);
    }

    /**
     * 用户登录
     *
     * @throws InterruptedException
     */
    protected void userLoginFromHttp(String username, String password) throws InterruptedException {
        Call<ResultVO<UserLoginVO>> resultVOCall = HttpApiService.getInstance().httpApi.userLogin(username, password);
        resultVOCall.enqueue(new Callback<ResultVO<UserLoginVO>>() {
            @Override
            public void onResponse(Call<ResultVO<UserLoginVO>> call, Response<ResultVO<UserLoginVO>> response) {
                // 为显示动效，模拟网络延迟1.5s
                DelayUtil.delayRunToUIThread(1500, new Runnable() {
                    @Override
                    public void run() {
                        centerPane.setDisable(false);
                        if (response.body().getCode() == 1) {
                            if (response.body().getData().getRole().equals(RoleConstant.ADMIN)) {
                                MovieClientApplication.showView(ManagerView.class);
                            } else {
                                MovieClientApplication.showView(ClientView.class);
                            }
                            UserInfoConstant.userId = response.body().getData().getUserId();
                            UserInfoConstant.role = response.body().getData().getRole();

                        } else {
                            errorLabel.setText(response.body().getMsg());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultVO<UserLoginVO>> call, Throwable throwable) {
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


    /**
     * 退出系统
     *
     * @param event
     */
    @FXML
    void rightTopCloseButtonAction(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 当右侧为不可编辑时，显示loading
        loadingBar.visibleProperty().bind(centerPane.disableProperty());
        loadingBar.managedProperty().bind(loadingBar.visibleProperty());
        // errorLabel 有值时才显示错误提示
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());
        // 监听用户名焦点变化
        userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                userNameTextField.validate();
            }
        });
        // 监听密码焦点变化
        passWordTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                passWordTextField.validate();
            }
        });

    }

}
