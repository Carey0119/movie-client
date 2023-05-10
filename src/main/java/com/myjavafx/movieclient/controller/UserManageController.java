package com.myjavafx.movieclient.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.http.HttpApiService;
import com.myjavafx.movieclient.http.PageVO;
import com.myjavafx.movieclient.http.ResultVO;
import com.myjavafx.movieclient.http.UserVO;
import com.myjavafx.movieclient.model.CommonColumnModel;
import com.myjavafx.movieclient.utils.ScreenUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import retrofit2.Call;
import retrofit2.Response;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class UserManageController extends BaseTableViewController<UserVO> implements Initializable {
    @FXML
    private TextField queryUsernameTextField;

    @FXML
    private DatePicker queryTimeDatePicker;

    @FXML
    private Button queryButton;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<UserVO> tableView;
    @FXML
    private StackPane mainStackPane;

    @FXML
    private JFXProgressBar loadingProgressBar;
    private final ObservableList<UserVO>  data = FXCollections.observableArrayList();
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        initPagination();
        // 从网络加载数据，页面不可编辑，显示loading
        loadingProgressBar.visibleProperty().bind(mainStackPane.disableProperty());
        loadingProgressBar.managedProperty().bind(loadingProgressBar.visibleProperty());
        mainStackPane.setPrefWidth(ScreenUtil.getFitWidth(1000, 0.9) - 200);
        mainStackPane.setPrefHeight(ScreenUtil.getFitHeight(600, 0.8) - 100);
    }

    private void getDataFromNet(Integer pageIndex) {
        this.pageNum = pageIndex;
        String username = null;
        String timeStr = null;
        try {
            username = this.queryUsernameTextField.getText().trim();
            timeStr = df.format(this.queryTimeDatePicker.getValue());
        }catch (Exception exception){

        }
        mainStackPane.setDisable(true);
        Call<ResultVO<PageVO<UserVO>>> resultVOCallback = HttpApiService.getInstance().httpApi.userList(this.pageNum,this.pageSize,username,timeStr);
        resultVOCallback.enqueue(new retrofit2.Callback<ResultVO<PageVO<UserVO>>>() {
            @Override
            public void onResponse(Call<ResultVO<PageVO<UserVO>>> call, Response<ResultVO<PageVO<UserVO>>> response) {
                if (response.body().getCode() == 1){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            data.clear();
                            data.addAll(response.body().getData().getList());
                            int paeCount = response.body().getData().getPages();
                            pagination.setPageCount(paeCount > 1 ? paeCount:1);
                        }
                    });

                }else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showMessageAlert(response.body().getMsg());
                        }
                    });
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainStackPane.setDisable(false);
                        tableView.refresh();
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultVO<PageVO<UserVO>>> call, Throwable throwable) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainStackPane.setDisable(false);
                        tableView.refresh();
                        showMessageAlert(throwable.toString());
                    }
                });
            }
        });

    }

    @FXML
    void queryButtonAction(ActionEvent event){
        getDataFromNet(1);
    }
    /**
     * 初始化分页控件
     */
    private void initPagination() {
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);
        pagination.setPageCount(2);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                getDataFromNet(pageIndex + 1);
                tableView.refresh();
                return new Pane();
            }
        });
    }


    /**
     * 初始化表格
     */
    private void initTableView() {
        tableView.setEditable(false);
        tableView.setTableMenuButtonVisible(true);
        // 去掉空白多于列
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        List<TableColumn<UserVO, Object>> list = createTableViewColumn(new ArrayList<CommonColumnModel>(8) {{
            add(CommonColumnModel.builder().columnName("序号").ifSort(false).width(50).refName("userNo").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("姓名").ifSort(false).width(150).refName("username").build());
            add(CommonColumnModel.builder().columnName("性别").ifSort(false).width(50).refName("sex").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("年龄").ifSort(false).width(50).refName("age").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("最近登录").ifSort(false).width(150).refName("lastLoginTime").build());
            add(CommonColumnModel.builder().columnName("注册时间").ifSort(false).width(150).refName("createTime").build());
            add(CommonColumnModel.builder().columnName("操作").ifSort(false).width(200).refName("").cssStyle("-fx-alignment: CENTER;").build());
        }});
        Button button = new Button();

        Callback<TableColumn<UserVO, Object>, TableCell<UserVO, Object>> cellFactory =
                new Callback<TableColumn<UserVO, Object>, TableCell<UserVO, Object>>() {
                    @Override
                    public TableCell call(final TableColumn<UserVO, Object> param) {
                        final TableCell<UserVO, Object> cell = new TableCell<UserVO, Object>() {
                            final Button btn = new Button("删除");{
                                btn.getStyleClass().add("btn");
                                btn.setOnAction(event -> {
                                    deleteItem(tableView.getItems().get(getIndex()).getUserId());
                                });
                            }

                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        list.get(list.size() - 1).setCellFactory(cellFactory);
        tableView.getColumns().addAll(list);
        tableView.setItems(data);

    }
    public void deleteItem(String userId){
        mainStackPane.setDisable(true);
        Call<ResultVO> resultVOCallback  =  HttpApiService.getInstance().httpApi.userDelete(userId);
        resultVOCallback.enqueue(new retrofit2.Callback<ResultVO>() {
            @Override
            public void onResponse(Call<ResultVO> call, Response<ResultVO> response) {
                if (response.body().getCode() == 1){
                    getDataFromNet(pageNum);
                }else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showMessageAlert(response.body().getMsg());
                        }
                    });
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainStackPane.setDisable(false);
                    }

                });
            }

            @Override
            public void onFailure(Call<ResultVO> call, Throwable throwable) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainStackPane.setDisable(false);
                        showMessageAlert("删除失败");
                    }

                });

            }
        });

    }


}
