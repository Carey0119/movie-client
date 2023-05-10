package com.myjavafx.movieclient.http;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
}
