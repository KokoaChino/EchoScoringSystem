package com.common.entity;

import lombok.Data;


@Data
public class RestBean<T> {

    private Integer status;
    private Boolean success;
    private T data;

    private RestBean(int status, boolean success, T data) {
        this.status = status;
        this.success = success;
        this.data = data;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, true, null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, true, data);
    }

    public static <T> RestBean<T> failure(int status) {
        return new RestBean<>(status, false, null);
    }

    public static <T> RestBean<T> failure(int status, T data) {
        return new RestBean<>(status, false, data);
    }
}