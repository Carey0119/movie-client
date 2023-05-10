package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    private Integer pages;
    private List<T> list;
}
