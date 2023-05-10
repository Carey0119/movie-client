package com.myjavafx.movieclient.http;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface HttpApi {
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 简易加密后的密码
     * @return
     */
    @POST("account/login")
    Call<ResultVO<UserLoginVO>> userLogin(@Query("username") String username, @Query("password") String password);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 简易加密后的密码
     * @param age 年龄
     * @param sex 性别
     * @param address 地址
     * @return
     */
    @POST("account/register")
    Call<ResultVO> userRegister(@Query("username") String username,
                                             @Query("password") String password,
                                             @Query("age") String age,
                                             @Query("sex") String sex,
                                             @Query("address") String address);

    /**
     * 用户列表
     * @param pageNum 页数
     * @param pageSize 第页大小
     * @param username 用户名
     * @param createTimeStr 用户注册时间 eg：2022-02-02
     * @return
     */
    @GET("user/list")
    Call<ResultVO<PageVO<UserVO>>> userList(@Query("pageNum") Integer pageNum,
                                @Query("pageSize") Integer pageSize,
                                @Query("username") String username,
                                @Query("createTimeStr") String createTimeStr);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @POST("user/delete")
    Call<ResultVO> userDelete(@Query("userId") String userId);

    /**
     * 用户列表
     * @param pageNum 页数
     * @param pageSize 第页大小
     * @param movieName 用户名
     * @param actor 演员
     * @return
     */
    @GET("movie/list")
    Call<ResultVO<PageVO<MovieVO>>> movieList(@Query("pageNum") Integer pageNum,
                                            @Query("pageSize") Integer pageSize,
                                            @Query("movieName") String movieName,
                                              @Query("actor") String actor);

    /**
     * 删除电影
     * @param movieId 电影id
     * @return
     */
    @POST("movie/delete")
    Call<ResultVO> movieDelete(@Query("movieId") String movieId);

    /**
     * 添加电影
     * @param movieName
     * @param movieDesc
     * @param actor
     * @param movieType
     * @return
     */
    @POST("movie/add")
    Call<ResultVO> addMovie(@Query("movieName") String movieName,
                            @Query("movieDesc")String movieDesc,
                            @Query("actor") String actor,
                            @Query("movieType")String movieType);

    /**
     * 添加点击量
     * @return
     */
    @POST("movie/click/add")
    Call<ResultVO> clickAdd(@Query("movieId")String movieId);

    /**
     * 添加评分
     * @return
     */
    @POST("movie/rating/add")
    Call<ResultVO> clickAdd(@Query("movieId")String movieId,
                            @Query("userId")String userId,
                            @Query("rating")String rating
    );
    /**
     * 数量统计
     * @return
     */
    @GET("statistics/num")
    Call<ResultVO<NumStatisticsVO>> numStatistics();

    /**
     * 用户年龄分布
     * @return
     */
    @GET("statistics/age/chart")
    Call<ResultVO<PageVO<ChartItemVO>>> ageChartStatistics();

    /**
     * 电影类型数量分布
     * @return
     */
    @GET("statistics/movie/type/chart")
    Call<ResultVO<PageVO<ChartItemVO>>> movieTypeStatistics();


}
