package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.myjavafx.movieclient.enums.ManagerEnumView;
import com.myjavafx.movieclient.http.HttpApiService;
import com.myjavafx.movieclient.http.MovieVO;
import com.myjavafx.movieclient.http.PageVO;
import com.myjavafx.movieclient.http.ResultVO;
import com.myjavafx.movieclient.model.CommonColumnModel;
import com.myjavafx.movieclient.utils.ScreenUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
public class MovieManagerController extends BaseTableViewController<MovieVO> implements Initializable {
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
    private ImageView mainPicImageView;

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
    private Label imagePathLabel;
    @FXML
    private Label errorLabel;


    private JFXAlert addMovieAlert;
    private JFXAlert movieDetailAlert;
    private String currentSelectedDetailMovieId;
    private File currentFile;


    private final ObservableList<MovieVO> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (location.getPath().contains("AddMovie")) {
            return;
        }
        if (location.getPath().contains("MovieDesc")) {
            return;
        }
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
    void addButtonAction(ActionEvent event) throws IOException {
//        showTips(mainStackPane);
        // 防止快速点击，弹出多个
        if (addMovieAlert != null){
            return;
        }
        addMovieAlert = showCustomPopView(ManagerEnumView.MODULE_MOVIE_ADD);
        errorLabel.setText("");
    }
    @FXML
    void closeButtonAction(ActionEvent event) {
        addMovieAlert.close();
        addMovieAlert = null;
    }
    @FXML
    void addMovieAction(ActionEvent event) {
        if (
                movieNameTextField.validate() &&
            actorTextField.validate() &&
                movieTypeTextField.validate() &&
                movieDescTextArea.validate()
        ){
            if (Strings.isBlank(imagePathLabel.getText())){
                errorLabel.setText("请选择图片");
            }else {
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), currentFile);
                MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", currentFile.getName(), imageBody);
                MultipartBody.Part movieNamePart = MultipartBody.Part.createFormData("movieName", movieNameTextField.getText());
                MultipartBody.Part movieDescPart = MultipartBody.Part.createFormData("movieDesc", movieDescTextArea.getText());
                MultipartBody.Part actorPart = MultipartBody.Part.createFormData("actor", actorTextField.getText());
                MultipartBody.Part movieTypePart = MultipartBody.Part.createFormData("movieType", movieTypeTextField.getText());
                Call<ResultVO> resultVOCall = HttpApiService.getInstance().httpApi.addMovie(movieNamePart,movieDescPart,actorPart,movieTypePart, imageBodyPart);
                resultVOCall.enqueue(new retrofit2.Callback<ResultVO>() {
                    @Override
                    public void onResponse(Call<ResultVO> call, Response<ResultVO> response) {
                        if (response.body().getCode() == 1){
                                addMovieAlert.close();
                                getDataFromNet(1);
                        }else {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    errorLabel.setText(response.body().getErr());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultVO> call, Throwable throwable) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                errorLabel.setText(throwable.toString());
                            }
                        });
                    }
                });
            }

        }
    }
    @FXML
    void closeDetailButtonAction(ActionEvent event){
        movieDetailAlert.close();
        movieDetailAlert = null;
    }

    @FXML
    void openFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getAbsolutePath());
        currentFile = file;
        imagePathLabel.setText(file.getAbsolutePath());


    }
    /**
     * 删除电影
     * @param movieId
     */
    public void deleteItem(String movieId) {
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
            add(CommonColumnModel.builder().columnName("操作").ifSort(false).width(200).refName("").cssStyle("-fx-alignment: CENTER;").build());
        }});


        Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>> cellFactory =
                new Callback<TableColumn<MovieVO, Object>, TableCell<MovieVO, Object>>() {
                    @Override
                    public TableCell call(final TableColumn<MovieVO, Object> param) {
                        final TableCell<MovieVO, Object> cell = new TableCell<MovieVO, Object>() {
                           final  HBox hBox = new HBox();{
                                Button deleteBtn = new Button("删除");{
                                    deleteBtn.getStyleClass().add("btn");
                                    deleteBtn.setOnAction(event -> {
                                        deleteItem(tableView.getItems().get(getIndex()).getMovieId());
                                    });
                                }

                                Button detailBtn = new Button("详情");{
                                    detailBtn.getStyleClass().add("btn");
                                    detailBtn.setOnAction(event -> {
                                        currentSelectedDetailMovieId = tableView.getItems().get(getIndex()).getMovieId();
                                        // 防止快速点击，弹出多个
                                        if (movieDetailAlert != null){
                                            return;
                                        }
                                        movieDetailAlert = showCustomPopView(ManagerEnumView.MODULE_MOVIE_DETAIL);
                                        MovieVO movieVO = tableView.getItems().get(getIndex());
                                        movieDescTestArea.setWrapText(true);
                                        movieDescTestArea.setText(movieVO.getMovieDesc());
                                        mainPicImageView.setFitWidth(135);
                                        mainPicImageView.setImage(new Image(movieVO.getMainPicPath()));
                                    });
                                }
                                hBox.getChildren().addAll(deleteBtn,detailBtn);
                               hBox.setSpacing(10);
                               hBox.setAlignment(Pos.CENTER);
                            }



                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setGraphic(hBox);
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


}
