package com.hlx.csom.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Wx
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/16 18:42
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wx {
    private String session_key;
    private String openid;
}
