package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieVO {
    private String movieId;
    private String movieNo;
    private String movieName;
    private String movieType;
    private String actor;
    private String score;
    private Integer clickCount;
    private String mainPicPath;
    private String movieDesc;
}
