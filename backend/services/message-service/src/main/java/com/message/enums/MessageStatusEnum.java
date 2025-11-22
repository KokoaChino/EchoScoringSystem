package com.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MessageStatusEnum {

    PENDING(0, "待处理"),
    SUCCESS(1, "处理成功"),
    FAILED(2, "处理失败");

    private final int code;
    private final String desc;
}