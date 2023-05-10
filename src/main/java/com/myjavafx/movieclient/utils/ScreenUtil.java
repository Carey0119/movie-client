package com.myjavafx.movieclient.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenUtil {
    /**
     * 获取合适窗口高度
     * @param expectHeight
     * @param scale
     * @return
     */
    public static double getFitHeight(double expectHeight,double scale){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double fitHeight = screenBounds.getHeight() * scale;
        if (fitHeight > expectHeight){
            expectHeight = fitHeight;
        }
        if (expectHeight > screenBounds.getHeight()){
            expectHeight = screenBounds.getHeight();
        }
        return expectHeight;
    }
    /**
     * 获取合适窗口宽度
     * @param expectWidth
     * @param scale
     * @return
     */
    public static double getFitWidth(double expectWidth,double scale){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double fitWidth = screenBounds.getWidth() * scale;
        if (fitWidth > expectWidth){
            expectWidth = fitWidth;
        }
        if (expectWidth > screenBounds.getWidth()){
            expectWidth = screenBounds.getWidth();
        }
        return expectWidth;
    }
}
