package com.hlx.csom.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @ClassName Type
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/15 17:02
 */

public enum TypeEnum {

    Driver(0,"驾驶员日常维护"),
    Tech(1,"技术培训"),
    Ana(2,"维修案例分析");

    @EnumValue
    private Integer key;

    @JsonValue
    private String value;

    TypeEnum(Integer key, String value){
        this.key=key;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public Integer getKey() {
        return key;
    }
}