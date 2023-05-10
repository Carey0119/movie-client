package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumStatisticsVO {
    private Integer peopleNum;
    private Integer movieNum;
    private Integer visitNum;
    private Integer scoreNum;
}
