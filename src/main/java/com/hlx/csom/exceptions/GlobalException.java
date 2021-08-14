package com.hlx.csom.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName GlobalException
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/15 17:25
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException {
    private Integer code = 400;
    private String msg = "未知错误!";

    public GlobalException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
