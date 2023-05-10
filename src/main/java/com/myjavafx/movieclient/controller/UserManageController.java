package com.myjavafx.movieclient.controller;

import com.jfoenix.controls.JFXProgressBar;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.http.UserVO;
import com.myjavafx.movieclient.model.CommonColumnModel;
import com.myjavafx.movieclient.utils.ScreenUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class UserManageController extends BaseTableViewController<UserVO> implements Initializable {
    @FXML
    private TextField queryUsername;

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
    private final ObservableList<UserVO> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        //initTableViewAction();
        initPagination();
        mainStackPane.setPrefWidth(ScreenUtil.getFitWidth(1000, 0.9) - 200);
        mainStackPane.setPrefHeight(ScreenUtil.getFitHeight(600, 0.8) - 100);

    }

    private void initPagination() {
        pagination.setCurrentPageIndex(0);
        pagination.setPageCount(getPaginationPageCount(data));
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return null;
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
            add(CommonColumnModel.builder().columnName("序号").ifSort(false).width(50).refName("userId").build());
            add(CommonColumnModel.builder().columnName("姓名").ifSort(false).width(150).refName("username").build());
            add(CommonColumnModel.builder().columnName("性别").ifSort(false).width(50).refName("sex").build());
            add(CommonColumnModel.builder().columnName("年龄").ifSort(false).width(50).refName("age").build());
            add(CommonColumnModel.builder().columnName("最近登录").ifSort(false).width(150).refName("lastLoginTime").build());
            add(CommonColumnModel.builder().columnName("注册时间").ifSort(false).width(150).refName("createTime").build());
            add(CommonColumnModel.builder().columnName("操作").ifSort(false).width(200).refName("").build());
        }});
        Callback<TableColumn<UserVO, Object>, TableCell<UserVO, Object>> cellFactory =
                new Callback<TableColumn<UserVO, Object>, TableCell<UserVO, Object>>() {
                    @Override
                    public TableCell call(final TableColumn<UserVO, Object> param) {
                        final TableCell<UserVO, Object> cell = new TableCell<UserVO, Object>() {
                            final Button btn = new Button("删除");

                            {
                                btn.setOnAction(event -> {
                                    tableView.getItems().remove(getIndex());
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


}
