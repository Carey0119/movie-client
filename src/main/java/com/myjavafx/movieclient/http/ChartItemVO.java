package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChartItemVO {
    private Integer chartY;
    private String chartX;
}
