package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.enums.ManagerEnumView;
import com.myjavafx.movieclient.http.*;
import com.myjavafx.movieclient.model.CommonColumnModel;
import com.myjavafx.movieclient.utils.ScreenUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.logging.log4j.util.Strings;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class ClientController extends  BaseTableViewController<MovieVO> implements Initializable{
    @FXML
    private Pagination pagination;

    @FXML
    private TableView<MovieVO> tableView;
    @FXML
    private StackPane mainStackPane;

    @FXML
    private JFXProgressBar loadingProgressBar;
    @FXML
    private TextField queryMovieTextField;
    @FXML
    private TextField queryActorTextField;
    @FXML
    private TextArea movieDescTestArea;

    @FXML
    private JFXTextArea movieDescTextArea;

    @FXML
    private Button picButton;

    @FXML
    private JFXTextField movieNameTextField;

    @FXML
    private JFXTextField actorTextField;

    @FXML
    private JFXTextField movieTypeTextField;
    @FXML
    private Label errorLabel;
    private String currentSelectedDetailMovieId;
    private File currentFile;


    private final ObservableList<MovieVO> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        initPagination();
        // 从网络加载数据，页面不可编辑，显示loading
        loadingProgressBar.visibleProperty().bind(mainStackPane.disableProperty());
        loadingProgressBar.managedProperty().bind(loadingProgressBar.visibleProperty());
        mainStackPane.setPrefWidth(ScreenUtil.getFitWidth(1000, 0.9) - 200);
        mainStackPane.setPrefHeight(ScreenUtil.getFitHeight(600, 0.8) - 100);
        MovieClientApplication.myStage.setWidth(ScreenUtil.getFitWidth(1000, 0.9));
        MovieClientApplication.myStage.setHeight(ScreenUtil.getFitHeight(600, 0.8));
        MovieClientApplication.myStage.centerOnScreen();

    }


    private void getDataFromNet(Integer pageIndex) {
        this.pageNum = pageIndex;
        String movie = null;
        String actor = null;
        try {
            movie = this.queryMovieTextField.getText().trim();
            actor = this.queryActorTextField.getText().trim();
        } catch (Exception exception) {

        }
        mainStackPane.setDisable(true);
        Call<ResultVO<PageVO<MovieVO>>> resultVOCallback = HttpApiService.getInstance().httpApi.movieList(this.pageNum, this.pageSize, movie, actor);
        resultVOCallback.enqueue(new retrofit2.Callback<ResultVO<PageVO<MovieVO>>>() {
            @Override
            public void onResponse(Call<ResultVO<PageVO<MovieVO>>> call, Response<ResultVO<PageVO<MovieVO>>> response) {
                if (response.body().getCode() == 1) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            data.clear();
                            data.addAll(response.body().getData().getList());
                            int paeCount = response.body().getData().getPages();
                            pagination.setPageCount(paeCount > 1 ? paeCount : 1);
                        }
                    });

                } else {
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
            public void onFailure(Call<ResultVO<PageVO<MovieVO>>> call, Throwable throwable) {
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
    void queryButtonAction(ActionEvent event) {
        getDataFromNet(1);
    }

    @FXML
    void closeButtonAction(ActionEvent event){
        System.exit(0);
    }
    @FXML
    void minButtonAction(ActionEvent event){
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    /**
     * 评分
     * @param movieId
     */
    public void ratingItem(String movieId) {
        mainStackPane.setDisable(true);
        Call<ResultVO> resultVOCallback = HttpApiService.getInstance().httpApi.movieDelete(movieId);
        resultVOCallback.enqueue(new retrofit2.Callback<ResultVO>() {
            @Override
            public void onResponse(Call<ResultVO> call, Response<ResultVO> response) {
                if (response.body().getCode() == 1) {
                    getDataFromNet(pageNum);
                } else {
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
        List<TableColumn<MovieVO, Object>> list = createTableViewColumn(new ArrayList<CommonColumnModel>(8) {{
            add(CommonColumnModel.builder().columnName("序号").ifSort(false).width(50).refName("movieNo").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("电影").ifSort(false).width(150).refName("movieName").build());
            add(CommonColumnModel.builder().columnName("主演").ifSort(false).width(50).refName("actor").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("类型").ifSort(false).width(50).refName("movieType").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("评分").ifSort(false).width(150).refName("score").build());
            add(CommonColumnModel.builder().columnName("点击量").ifSort(false).width(150).refName("clickCount").build());
            add(CommonColumnModel.builder().columnName("评分").ifSort(false).width(200).refName("").cssStyle("-fx-alignment: CENTER;").build());
            add(CommonColumnModel.builder().columnName("操作").ifSort(false).width(200).refName("").cssStyle("-fx-alignment: CENTER;").build());
        }});


        Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>> cellFactory =
                new Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>>() {
                    @Override
                    public TableCell call(final TableColumn<MovieVO, Object> param) {
                        final TableCell<MovieVO, Object> cell = new TableCell<MovieVO, Object>() {
                            final  Button detailBtn = new Button("简介");{
                                    detailBtn.getStyleClass().add("btn");
                                    detailBtn.setOnAction(event -> {
                                        currentSelectedDetailMovieId = tableView.getItems().get(getIndex()).getMovieId();
                                        // 弹出框对应的FXML文件
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource(ManagerEnumView.MODULE_MOVIE_DETAIL.fxmlPath));
                                        try {
                                            Parent parent = loader.load();
                                            JFXAlert  movieDetailAlert = showCustomPopView2(parent);
                                            MovieVO movieVO = tableView.getItems().get(getIndex());
                                            MovieDetailController target = loader.getController();
                                            target.setDesc(movieVO.getMovieDesc());
                                            target.setAlertView(movieDetailAlert);
                                            HttpApiService.getInstance().httpApi.clickAdd(currentSelectedDetailMovieId).execute();
                                            getDataFromNet(pageNum);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    });
                                }

                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setGraphic(detailBtn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        list.get(list.size() - 1).setCellFactory(cellFactory);

        Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>> cellFactory2 =
                new Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>>() {
                    @Override
                    public TableCell call(final TableColumn<MovieVO, Object> param) {
                        final TableCell<MovieVO, Object> cell = new TableCell<MovieVO, Object>() {
                             final   ComboBox comboBox = new ComboBox();
                                {
                                    comboBox.getItems().addAll(
                                            "1",
                                            "2",
                                            "3",
                                            "4",
                                            "5"
                                    );
                                    comboBox.setEditable(false);
                                    comboBox.setOnAction(event -> {

                                        try {
                                            // 评分
                                            HttpApiService.getInstance().httpApi.clickAdd(
                                                    tableView.getItems().get(getIndex()).getMovieId(),
                                                    UserInfoConstant.userId,
                                                    comboBox.getSelectionModel().getSelectedItem().toString()
                                            ).execute();
                                            // 刷新列表
                                            getDataFromNet(pageNum);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setGraphic(comboBox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        list.get(list.size() - 2).setCellFactory(cellFactory2);


        tableView.getColumns().addAll(list);
        tableView.setItems(data);

    }

}

