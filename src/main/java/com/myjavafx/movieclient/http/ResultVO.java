package com.myjavafx.movieclient.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
    private String err;

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.msg = message;
        this.err = "";
    }

    public ResultVO<T> setData(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return this.data == null ? (T) new HashMap<String, Object>(0) : data;
    }

    public ResultVO<T> setErr(String err) {
        this.err = err;
        return this;
    }

    public String getErr() {
        return this.err == null ? "" : this.err;
    }

    public ResultVO<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
