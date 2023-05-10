package com.myjavafx.movieclient.controller;

import com.myjavafx.movieclient.http.*;
import com.myjavafx.movieclient.utils.ScreenUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class StatisticsController implements Initializable {

    @FXML
    private GridPane mainGridPan;
    @FXML
    private ImageView peopleImageView;

    @FXML
    private Label peopleNumLabel;

    @FXML
    private ImageView movieImageView;

    @FXML
    private Label movieNumLabel;

    @FXML
    private ImageView viewImageView;

    @FXML
    private Label viewNumLabel;

    @FXML
    private ImageView scoreNumImageView;

    @FXML
    private Label scoreNumLabel;

    @FXML
    private BarChart<String, Number>  movieTypeBarChart;

    @FXML
    private LineChart<String, Number> ageLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        peopleImageView.setImage(new Image(getClass().getResourceAsStream("/images/statistics/people.png")));
        movieImageView.setImage(new Image(getClass().getResourceAsStream("/images/statistics/movie.png")));
        viewImageView.setImage(new Image(getClass().getResourceAsStream("/images/statistics/visit.png")));
        scoreNumImageView.setImage(new Image(getClass().getResourceAsStream("/images/statistics/score.png")));
        mainGridPan.setPrefWidth(ScreenUtil.getFitWidth(1000, 0.9) - 200);
        mainGridPan.setPrefHeight(ScreenUtil.getFitHeight(600, 0.8) - 100);
        getNumStatistics();
        getMovieTypeChartStatistics();
        getAgeChartStatistics();
    }

    /**
     * 数量统计
     */
    private void getNumStatistics(){
      Call<ResultVO<NumStatisticsVO>> resultVOCall = HttpApiService.getInstance().httpApi.numStatistics();
      resultVOCall.enqueue(new Callback<ResultVO<NumStatisticsVO>>() {
          @Override
          public void onResponse(Call<ResultVO<NumStatisticsVO>> call, Response<ResultVO<NumStatisticsVO>> response) {
              if (response.body().getCode() == 1){
                  Platform.runLater(new Runnable() {
                      @Override
                      public void run() {
                          peopleNumLabel.setText(response.body().getData().getPeopleNum().toString());
                          movieNumLabel.setText(response.body().getData().getMovieNum().toString());
                          scoreNumLabel.setText(response.body().getData().getScoreNum().toString());
                          viewNumLabel.setText(response.body().getData().getVisitNum().toString());

                      }
                  });

              }
          }

          @Override
          public void onFailure(Call<ResultVO<NumStatisticsVO>> call, Throwable throwable) {

          }
      });
    }

    /**
     * 年龄统计
     */
    private void getAgeChartStatistics(){
        Call<ResultVO<PageVO<ChartItemVO>>> resultVOCall = HttpApiService.getInstance().httpApi.ageChartStatistics();
        resultVOCall.enqueue(new Callback<ResultVO<PageVO<ChartItemVO>>>() {
            @Override
            public void onResponse(Call<ResultVO<PageVO<ChartItemVO>>> call, Response<ResultVO<PageVO<ChartItemVO>>> response) {
                if (response.body().getCode() == 1){
                    List<ChartItemVO> chartItemVOList = response.body().getData().getList();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
                            for (ChartItemVO chartItemVO : chartItemVOList) {
                                series.getData().add(new XYChart.Data<>(chartItemVO.getChartX(),chartItemVO.getChartY()));
                            }
                            ageLineChart.getData().add(series);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResultVO<PageVO<ChartItemVO>>> call, Throwable throwable) {

            }
        });
    }

    /**
     * 电影类型统计
     */
    private void getMovieTypeChartStatistics(){
        Call<ResultVO<PageVO<ChartItemVO>>> resultVOCall = HttpApiService.getInstance().httpApi.movieTypeStatistics();
        resultVOCall.enqueue(new Callback<ResultVO<PageVO<ChartItemVO>>>() {
            @Override
            public void onResponse(Call<ResultVO<PageVO<ChartItemVO>>> call, Response<ResultVO<PageVO<ChartItemVO>>> response) {
                if (response.body().getCode() == 1){
                    List<ChartItemVO> chartItemVOList = response.body().getData().getList();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
                            for (ChartItemVO chartItemVO : chartItemVOList) {
                                series.getData().add(new XYChart.Data<>(chartItemVO.getChartX(),chartItemVO.getChartY()));
                            }
                            movieTypeBarChart.getData().add(series);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultVO<PageVO<ChartItemVO>>> call, Throwable throwable) {

            }
        });
    }

}
