package com.myjavafx.movieclient.enums;

public enum ManagerEnumView {
    /**
     * 数据统计
     */
    MODULE_STATISTICS("数据统计", "/fxml/Statistics.fxml"),
    /**
     * 电影管理
     */
    MODULE_MOVIE_MANAGE("电影管理", "/fxml/MovieManage.fxml"),
    /**
     * 用户管理
     */
    MODULE_USER_MANAGE("用户管理", "/fxml/UserManage.fxml");
    /**
     * 标题
     */
    public String title;
    /**
     * fxml 路径
     */
    public String fxmlPath;

    /**
     * 构造函数
     *
     * @param title
     * @param fxmlPath
     */
    ManagerEnumView(String title, String fxmlPath) {
        this.title = title;
        this.fxmlPath = fxmlPath;
    }
}
