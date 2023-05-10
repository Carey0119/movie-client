package com.myjavafx.movieclient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonColumnModel {
    protected String columnName;
    private double width;
    private boolean ifSort;
    private String refName;
    private String cssStyle;
}
