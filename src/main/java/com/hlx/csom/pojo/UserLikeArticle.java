package com.hlx.csom.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName UserLikeArticle
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/14 11:21
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLikeArticle {

    private String userId;
    private Integer articleId;

}
