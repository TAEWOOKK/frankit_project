package com.app.model;

import com.app.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class SuccessResponse {

    private String resultCode;

    public SuccessResponse(ResultCode resultCode) {
        this.resultCode = resultCode.getCode();
    }

}
