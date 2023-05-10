package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
public class UserVO {
    private Integer userNo;
    private String userId;
    private String username;
    private String age;
    private String sex;
    private String createTime;
    private String lastLoginTime;
}
