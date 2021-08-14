package com.hlx.csom.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Photo
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/25 15:36
 */
@Setter
@Getter
@NoArgsConstructor
public class Photo {

    private String url;
    private String alt;
    private String href;


    public Photo(String url, String alt, String href) {
        this.url = url;
        this.alt = alt;
        this.href = href;
    }
}
