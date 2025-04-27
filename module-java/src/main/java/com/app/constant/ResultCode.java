package com.app.constant;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS("success")
    ;

    ResultCode(String code) {
        this.code = code;
    }

    private String code;

}
