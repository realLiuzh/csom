package com.hlx.csom.base;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName ResultInfo
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/13 13:19
 */
@Getter
@Setter
@AllArgsConstructor
public class ResultInfo {
    private Integer code=200;
    private String msg="success";
    private Object result;

    public ResultInfo() {
    }

    public ResultInfo(Object result) {
        this.result = result;
    }

    public ResultInfo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
