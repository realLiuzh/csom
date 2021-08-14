package com.hlx.csom.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName ArticlePhoto
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/20 20:23
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePhoto {
    private Integer articleId;
    private String photoName;
}
