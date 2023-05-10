package com.myjavafx.movieclient.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;


public class HttpApiService {
    public static HttpApi httpApi;
    private static HttpApiService instance;
    private static Retrofit retrofit;

    public static HttpApiService getInstance() {
        if (instance == null) {
            synchronized (HttpApiService.class) {
                if (instance == null) {
                    instance = new HttpApiService();
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://127.0.0.1:8089/movie/v1/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                    httpApi = retrofit.create(HttpApi.class);
                }
            }

        }
        return instance;
    }


}
