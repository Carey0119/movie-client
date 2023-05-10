package com.myjavafx.movieclient.controller;

import com.myjavafx.movieclient.http.UserVO;
import com.myjavafx.movieclient.model.CommonColumnModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseTableViewController<T> {

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
            tableColumn.setCellValueFactory(new PropertyValueFactory(commonColumnModel.getRefName()) );
            columnList.add(tableColumn);
        }
        return columnList;

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
