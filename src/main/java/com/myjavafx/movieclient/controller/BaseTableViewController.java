package com.myjavafx.movieclient.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.myjavafx.movieclient.MovieClientApplication;
import com.myjavafx.movieclient.enums.ManagerEnumView;
import com.myjavafx.movieclient.http.UserVO;
import com.myjavafx.movieclient.model.CommonColumnModel;
import com.myjavafx.movieclient.utils.SpringFXMLLoader;
import com.myjavafx.movieclient.utils.SpringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.util.Strings;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseTableViewController<T> {

    public Integer pageNum = 1;
    public Integer pageSize = 20;

    /**
     * 快速生成自定义列
     * @param list
     * @return
     */
    public List<TableColumn<T, Object>> createTableViewColumn(List<CommonColumnModel> list){
        if (list == null){
            return Collections.EMPTY_LIST;
        }
        List<TableColumn<T,Object>> columnList = new ArrayList<>();
        for (CommonColumnModel commonColumnModel : list) {
            TableColumn tableColumn = new TableColumn(commonColumnModel.getColumnName());
            tableColumn.setSortable(commonColumnModel.isIfSort());
            tableColumn.setPrefWidth(commonColumnModel.getWidth());
            if (Strings.isNotBlank(commonColumnModel.getCssStyle())){
                tableColumn.setStyle(commonColumnModel.getCssStyle());
            }
            tableColumn.setCellValueFactory(new PropertyValueFactory(commonColumnModel.getRefName()) );
            columnList.add(tableColumn);
        }
        return columnList;

    }

    /**
     * 弹框提示
     * @param message
     */
    public void showMessageAlert(String message){
        JFXDialogLayout layout = new JFXDialogLayout();
        Label label = new Label(message);
        layout.setBody(label);
        JFXAlert<Void> alert = new JFXAlert<>(MovieClientApplication.myStage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);
        alert.show();
    }


    /**
     * 自定义弹框
     * @param view
     */
    public JFXAlert showCustomPopView(ManagerEnumView view) {
        try {
           // 弹出框对应的FXML文件
            Parent parent = SpringUtils.getBean(SpringFXMLLoader.class).load(view.fxmlPath);
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(parent);
            JFXAlert<Void> alert = new JFXAlert<>(MovieClientApplication.myStage);
            alert.setOverlayClose(false);
            alert.setHideOnEscape(false);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.show();
            return alert;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 获取页码
     * @param list
     * @return
     */
    public int getPaginationPageCount(List list){
        if (Objects.isNull(list) || list.size() == 0){
            return 1;
        }
        int pageCount = list.size() / 10;
        if (pageCount < 1){
            return 1;
        }
        return pageCount;
    }
}
